
% - regionsObjPath: path to the file containing the
% regions in the objective space. The format has to be:
% obj1_min:obj1_max,obj2_min:obj2_max,obj3_min:obj3_max,
% - objNames (optional). Default: {'obj1','obj2'}
% - plotPath: path where to write the plot (default: '', i.e. just display,
% don't write)
% - alpha (optional). Opacity in plots. Default 0.5

function [regions,volumes,paramRegions,paramVolumes,meanVol,meanSens,maxSens] = front_2d(regionsObjPath, nContinuousParam, nDiscreteParam, ...
    regionsParamPath,objNames, plotPath,alpha)

if(nargin < 7)
    alpha = 0.5;
    if(nargin < 6)
        plotPath='';
        if(nargin < 5)
            objNames = {'obj1','obj2'};
        end
    end
end

[regions,volumes] = readRegionsFile(regionsObjPath,2);
[paramRegions,paramVolumes] = readParamRegionsFile(regionsParamPath,nContinuousParam,nDiscreteParam);

%fprintf('\n%d %d', size(volumes), size(paramVolumes));

sensitivities=volumes./paramVolumes;

meanVol=mean(volumes);
meanSens=mean(sensitivities);
maxSens=max(sensitivities);

figure
xdata=zeros(4,size(regions,1));
ydata=zeros(4,size(regions,1));
zdata=ones(4,size(regions,1));

for i = 1:size(regions,1)
    
    obj1m=regions(i,1);
    obj1M=regions(i,2);
    obj2m=regions(i,3);
    obj2M=regions(i,4);
    
    xdata(:,i) = [obj1m; obj1M; obj1M; obj1m];
    ydata(:,i) = [obj2m; obj2m; obj2M; obj2M];
    
end

colors = zeros(size(xdata));

for i=1:4
    colors(i,:)=sensitivities';
end
displayString = '. Colouring based on sensitivity.';

patch(xdata,ydata,zdata,colors,'FaceAlpha',alpha,'EdgeColor',[120 120 120]/255);

title(strcat(['Pareto Front for objectives ',objNames{1}, ' and ',objNames{2},displayString]));
xlabel(objNames{1});
ylabel(objNames{2});

colorbar

if(~strcmp(plotPath,''))
    saveas(gcf,plotPath,'png');
    saveas(gcf,plotPath,'fig');
end
end
