library(ggplot2)
library(reshape2)
library(psych)
library(pastecs)
library(car)
library(pgirmess)
library(dunn.test)
library(cowplot)
library(effsize)


#Step 1: Input library
#         source("RODES.R")
#Step 2: Prepare data for an indicator-sensitivity combination
#       prepareDataForIndicator(tolerances=c("001","002", "005"), leniencies=c("000", "005", "010"),
                                #indicatorFile, sensitivityFile="sensitivity", 
                                #indicatorName="I", weight=0.5)
#Step 3:  Do boxplots for an indicator-sensitivity combination
#         doBoxPlotForIndicator <- function(indicatorFile, sensitivityFile, indicatorName="I", weight=0.5)



#********************* Read data ********************
readData <- function(file){
  fileName <- file  
  #read data
  rawData <- read.csv(file=fileName, header = FALSE, dec='.', sep = ',')
  names(rawData) <- c("NSGA", "RS")
  return (rawData)
} 


#********************* BoxPlot for an indiciator (i.e., E, IGD) ********************
prepareDataForIndicator <- function(tolerances=c("001","002", "005"), leniencies=c("000", "005", "010"),
                                    indicatorFile, sensitivityFile="sensitivity", 
                                    indicatorName="I", weight=0.5){  
  baseDir="Google/StatisticalAnalysis/"
  
  #find max for both
  maxIndicator    = -1;
  maxSensitivity  = -1;
  for (tolerance in tolerances){
    for (leniency in leniencies){
      experimentDir = paste0(baseDir, tolerance, "_", leniency);
      #print(experimentDir)
      #read indicator data
      indicatorData         <- read.csv(file=paste0(experimentDir,"/",indicatorFile,'.csv'), header = FALSE, dec='.', sep = ',')
      names(indicatorData)  <- c("GA","RS")
      
      #read sensitivity data
      sensitivityData         <- read.csv(file=paste0(experimentDir,"/",sensitivityFile), header = FALSE, dec='.', sep = ',')
      names(sensitivityData)  <- c("SENS_GA", "SENS_RS")  
      
      if (maxIndicator < max(indicatorData)){
        maxIndicator <- max(indicatorData)
      }
      if (maxSensitivity < max(sensitivityData)){
        maxSensitivity <- max(sensitivityData)
      }
    }
  }
  
  data <- data.frame(rep(c("GA","RS"),c(30,30)))
  names(data) <- c("Algorithm")
  index=2;
  for (tolerance in tolerances){
    for (leniency in leniencies){
      experimentDir = paste0(baseDir, tolerance, "_", leniency);
      #print(experimentDir)
      #read indicator data
      indicatorData         <- read.csv(file=paste0(experimentDir,"/",indicatorFile,'.csv'), header = FALSE, dec='.', sep = ',')
      names(indicatorData)  <- c("GA","RS")
      
      #read sensitivity data
      sensitivityData         <- read.csv(file=paste0(experimentDir,"/",sensitivityFile), header = FALSE, dec='.', sep = ',')
      names(sensitivityData)  <- c("SENS_GA", "SENS_RS")  
      
      dfGA <- data.frame(indicatorData$GA, sensitivityData$SENS_GA)
      dfRS <- data.frame(indicatorData$RS, sensitivityData$SENS_RS)
      
      dfGA$V <- apply(dfGA,1,function(x){weight*x[1]/maxIndicator + (1-weight)*x[2]/maxSensitivity})
      dfRS$V <- apply(dfRS,1,function(x){weight*x[1]/maxIndicator + (1-weight)*x[2]/maxSensitivity})
      
      data[,index] <- c(dfGA$V, dfRS$V)
      colnames(data)[index] <- paste0(tolerance,'_',leniency)
      #data[,index+1] <- dfRS$V
      #colnames(data)[index+1] <- paste0(tolerance,'_',leniency)
      #print(data)
      index = index + 1
    }
  }
  
  assign("data", data, envir=.GlobalEnv)
}


#********************* BoxPlot for CPU Time ********************
doBoxPlotCPUTime <- function(fileName, indicator){  
  #read data
  data <- read.csv(file=fileName, header = FALSE, dec='.', sep = ',')
  names(data) <- c("Google File System", "Workstation Cluster")
  
  meltData <- melt(data)
  #  boxPlot <- ggplot(meltData, aes(factor(variable), value)) 
  #  boxPlot + geom_boxplot() + facet_wrap(~variable, scale="free")
  boxPlot <- ggplot(meltData, aes(x=factor(variable), y=value)) 
  #p + geom_boxplot() #+ facet_wrap(~variable, scale="free") 
  boxPlot + geom_boxplot() + labs(x="Case Study", y=indicator) 
}


#********************* BoxPlot for an indiciator (i.e., E, IGD) ********************
doBoxPlotForIndicator <- function(tolerances=c("001","002", "005"), leniencies=c("000", "005", "010"),
                                  indicatorFile, sensitivityFile="sensitivity", 
                                  indicatorName="I", weight=0.5){
  
  prepareDataForIndicator(tolerances, leniencies, indicatorFile, sensitivityFile, indicatorName, weight)
  
  meltData <- melt(data)  
  assign("meltData", meltData, envir=.GlobalEnv)  
  #print(meltData)
  
  p <- ggplot(meltData, aes(x=factor(variable), y=value))
  p <- p + geom_boxplot(aes(fill=Algorithm))
  p <- p + scale_fill_grey(start=0.5) + theme_minimal()
  p <- p + labs(x="Variant", y=indicatorFile)
  #Add x-labels, y-labels, title, change legend heading, add a jitter
  #p <- p + xlab("#Variant") + ylab("Utility") + ggtitle(paste("Event", num))
  p <- p + theme(legend.title=element_text(colour="chocolate", size=12, face="bold"))
  p <- p + guides(fill=guide_legend(title=""))
  p <- p + scale_y_continuous(limits = c(0.1, 0.85))
  
  #display
  p
  filename <-  ggsave(file= paste0(indicatorFile,'_',weight,".pdf"))
  filename <-  ggsave(file= paste0(indicatorFile,'_',weight,".svg"))
}


#********************* Wilcoxon Rank Rank ********************
doWilcoxon <- function(tolerances, leniencies,
                        indicatorFile, sensitivityFile="sensitivity", 
                        indicatorName="I", weight=0.5){
  #read data
  prepareDataForIndicator(tolerances, leniencies, indicatorFile, sensitivityFile, indicatorName, weight)  

  index=2;  
  for (tolerance in tolerances){
    for (leniency in leniencies){
    
      wilcoxonData <- data.frame(rep(1:30,1), data[1], data[index])
      names(wilcoxonData) <- c("ID", "Algorithm", "Utility")
      dataWide <- dcast(data=wilcoxonData, ID ~Algorithm)  
      dataWide$ID <- NULL
      GA <- dataWide$GA
      RS <- dataWide$RS
      print(wilcox.test(GA, RS))
      print(wilcox.test(dataWide$GA, dataWide$RS))
      index = index + 1
    }
  }
}


#*** Effect size: Vargha & Delaney for meleted data*****
#***********************************************
doEffectSizeVD <- function(tolerances, leniencies,
                           indicatorFile, sensitivityFile="sensitivity", 
                           indicatorName="I", weight=0.5){
  #read data
  prepareDataForIndicator(tolerances, leniencies, indicatorFile, sensitivityFile, indicatorName, weight)  
  
  effectResult <- vector()

  index=2;  
  for (tolerance in tolerances){
    for (leniency in leniencies){
      
      effectSizeData <- data.frame(rep(1:30,1), data[1], data[index])
      names(effectSizeData) <- c("ID", "Algorithm", "Utility")
      dataWide <- dcast(data=effectSizeData, ID ~Algorithm)  
      dataWide$ID <- NULL
      GA <- dataWide$GA
      RS <- dataWide$RS
      
      print(VD.A(-GA,-RS))
      #print(VD.A(-dataWide[,1],-dataWide[,2]))
      index = index + 1
    }
  }
}
#########################################################
#########################################################
##
## OTHER FUNCTIONS
##
#########################################################
#########################################################


#********************* Plot histogram ********************
doHistogram <- function(file, columnName, widthBin){
  #read data
  dataHist <- readData(file)
  names(dataHist) <- c("NSGA", "SPEA2", "MOCell", "RS")
  #print(dataHist[[columnName]])
  #init plot
  histogram <- ggplot(dataHist, aes(NSGA))
  #create histogram
  histogram + theme(legend.position="none") +
    geom_histogram(binwidth=widthBin, aes(y=..density..), colour="black", fill="white") +
    labs(x="NSGA", y="Density") +
    #create normal curve
    stat_function(fun=dnorm, args=list(mean=mean(dataHist[[columnName]], na.rm=TRUE), sd=sd(dataHist[[columnName]], na.rm=TRUE)), colour="black", size=1)
  #  hgData <- histogram
}


#********************* Plot QQ ********************
doQQPlot <- function (file, columnName){
  #read data
  data <- readData(file)
  names(data) <- c("NSGA", "SPEA2", "MOCell", "RS")
  #init plot  
  qqPlot <- qplot(sample=data[[columnName]], stat="qq")
  qqPlot
}


#********************* Normal Data ********************
quantifyingNormality <- function(file){
  #read data
  #read data
  data <- readData(file)
  names(data) <- c("NSGA", "SPEA2", "MOCell", "RS")  
  #  describe(dlf$day1)
  stats <- stat.desc(cbind(data$NSGA, data$SPEA2, data$MOCell, data$RS), basic=FALSE, norm=TRUE)
  round(stats, digits=3)
}


#********************* Shapiro-Wilks normality test ********************
doShapiro <- function(file){
  #read data
  data <- readData(file)
  names(data) <- c("NSGA", "SPEA2", "MOCell", "RS")
  #print(data)
  #quantify normality
  round(stat.desc(cbind(data$NSGA, data$SPEA2, data$MOCell, data$RS), basic=FALSE, norm=TRUE), digits=3)
  
  #run shapiro
  shNSGA    <- shapiro.test(data$NSGA)
  shSPEA    <- shapiro.test(data$SPEA2)
  shMOCELL  <- shapiro.test(data$MOCell)
  shRANDOM  <- shapiro.test(data$RS)  
  
  #print results
  print(shNSGA); print(shSPEA); print(shMOCELL); print(shRANDOM)
}


#********************* Kruskal Wallis ********************
doKruskalWallis <- function(file, removeRS){
  #read data
  data <- readData(file)
  names(data) <- c("NSGA", "SPEA2", "MOCell", "RS")
  #transform columns  
  data <- data.frame(data$RS, data$NSGA, data$SPEA2, data$MOCell)
  names(data) <- c("RS", "NSGA", "SPEA2", "MOCell")
  if (removeRS){
    data[[1]] <- NULL    
  }
  View(data)
  #transform to two columns Algorithm and Value
  meltData <- melt(data)
  names(meltData) <- c("Algorithm", "QIValue")
  #View(meltData)
  
  #run kruskal wallis
  kruskalResults <- kruskal.test(QIValue ~ Algorithm, data=meltData)
  kruskalResultsFrame <- data.frame(unlist(kruskalResults))
  View(kruskalResultsFrame)
  print (kruskalResults)
  
  #add ranks to data frame
  meltData$Ranks <- rank(meltData$QIValue)
  by(meltData$Ranks, meltData$Algorithm, mean)
  
  #run pairwise comparisons 1
  kruskalMCResults <- kruskalmc (QIValue ~ Algorithm, data=meltData)
  print (kruskalMCResults)
  #kruskalMCResults <- kruskalmc (QIValue ~ Algorithm, data=meltData, cont="two-tailed")
  #print (kruskalMCResults)  
  
  #run pairwise comparisons 2  
  dunnResults <- dunn.test(x=meltData$QIValue, g=meltData$Algorithm, kw=TRUE, method="bonferroni")
  print (dunnResults)
  
  #  print(mean(data[[1]]))
  
  #  empty <- c("")
  #  write.table(x=kruskalResultsFrame, file="output43.csv", append=F, sep=",", quote=F,row.names=T, eol="\n")    
  #  write.table(x=empty, file="output43.csv", append=T, sep=",", quote=F,row.names=F)  
  #  write.table(x=kruskalMCResults, file="output43.csv", append=T, sep=",", quote=F,row.names=F, eol="\n")  
  #  write.table(x=empty, file="output43.csv", append=T, sep=",", quote=F,row.names=F)  
  #  write.table(x=dunnResults, file="output43.csv", append=T, sep=",", quote=F,row.names=F)
}
