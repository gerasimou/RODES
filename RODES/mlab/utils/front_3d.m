
% - regionsObjPath: path to the file containing the
% regions in the objective space.
% - objNames (optional). Default: {'obj1','obj2','obj3'}
% - alpha (optional). Opacity in plots. Default 0.5
% - plotPath: path where to write the plot (default: '', i.e. just display,
% don't write)

function [regions,volumes,paramRegions,paramVolumes,meanVol,meanSens,maxSens] = front_3d(regionsObjPath, nContinuousParam, nDiscreteParam, ...
    regionsParamPath, objNames, plotPath, alpha)

if(nargin < 7)
    alpha = 0.5;
    if(nargin < 6)
        plotPath='';
        if(nargin < 5)
            objNames = {'obj1','obj2','obj3'};
        end
    end
end

[regions,volumes] = readRegionsFile(regionsObjPath,3);

[paramRegions,paramVolumes] = readParamRegionsFile(regionsParamPath,nContinuousParam,nDiscreteParam);
sensitivities=volumes./paramVolumes;

meanVol=mean(volumes);
meanSens=mean(sensitivities);
maxSens=max(sensitivities);

figure
xdata=zeros(4,6*size(regions,1));
ydata=zeros(4,6*size(regions,1));
zdata=zeros(4,6*size(regions,1));

for i = 1:size(regions,1)
    obj1m=regions(i,1);
    obj1M=regions(i,2);
    obj2m=regions(i,3);
    obj2M=regions(i,4);
    obj3m=regions(i,5);
    obj3M=regions(i,6);
    
    p=[obj1m obj2m obj3m; obj1M obj2m obj3m; obj1M obj2M obj3m; obj1m obj2M obj3m;
        obj1m obj2m obj3M; obj1M obj2m obj3M; obj1M obj2M obj3M; obj1m obj2M obj3M];
    
    idxs=(i-1)*6+1:i*6;
    xdata(:,idxs) = [p(1,1) p(1,1) p(1,1) p(2,1) p(3,1) p(5,1);
        p(2,1) p(4,1) p(2,1) p(3,1) p(4,1) p(6,1);
        p(3,1) p(8,1) p(6,1) p(7,1) p(8,1) p(7,1);
        p(4,1) p(5,1) p(5,1) p(6,1) p(7,1) p(8,1);
        ];
    
    ydata(:,idxs) = [p(1,2) p(1,2) p(1,2) p(2,2) p(3,2) p(5,2);
        p(2,2) p(4,2) p(2,2) p(3,2) p(4,2) p(6,2);
        p(3,2) p(8,2) p(6,2) p(7,2) p(8,2) p(7,2);
        p(4,2) p(5,2) p(5,2) p(6,2) p(7,2) p(8,2);
        ];
    
    zdata(:,idxs) = [p(1,3) p(1,3) p(1,3) p(2,3) p(3,3) p(5,3);
        p(2,3) p(4,3) p(2,3) p(3,3) p(4,3) p(6,3);
        p(3,3) p(8,3) p(6,3) p(7,3) p(8,3) p(7,3);
        p(4,3) p(5,3) p(5,3) p(6,3) p(7,3) p(8,3);
        ];
    
end


colors = zeros(size(xdata));
for i=1:size(colors,1)
    for j=1:size(xdata,2)
        colors(i,j)=sensitivities(ceil(j./6));
    end
end
displayString = '. Colouring based on sensitivity.';




patch(xdata,ydata,zdata,colors,'FaceAlpha',alpha,'EdgeColor',[120 120 120]/255)

title(strcat(['Pareto Front for objectives ',objNames{1}, ', ',objNames{2}, ' and ',objNames{3},displayString]));
xlabel(objNames{1});
ylabel(objNames{2});
zlabel(objNames{3});

view(50,50)
if(~strcmp(plotPath,''))
    saveas(gcf,plotPath,'png');
    saveas(gcf,plotPath,'fig');
end
end
