
% - regionsObjPath: path to the file containing the
% regions in the objective space. The format has to be:
% obj1_min:obj1_max,obj2_min:obj2_max,obj3_min:obj3_max,
% - objNames (optional). Default: {'obj1','obj2'}
% - alpha (optional). Opacity in plots. Default 0.5
% - plotPath: path where to write the plot (default: '', i.e. just display,
% don't write)


function param_plot_3d(volumes,paramRegions,paramVolumes,  paramNames, plotPath, alpha)

if(nargin < 6)
    alpha = 0.5;
    if(nargin < 5)
        plotPath='';
        if(nargin < 4)
            paramNames = {'param1','param2','param3'};
        end
    end
end

sensitivities=volumes./paramVolumes;

figure
xdata=zeros(4,6*size(paramRegions,1));
ydata=zeros(4,6*size(paramRegions,1));
zdata=zeros(4,6*size(paramRegions,1));

for i = 1:size(paramRegions,1)
    obj1m=paramRegions(i,1);
    obj1M=paramRegions(i,2);
    obj2m=paramRegions(i,3);
    obj2M=paramRegions(i,4);
    obj3m=paramRegions(i,5);
    obj3M=paramRegions(i,6);
    
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

title(strcat(['Parameter space for ',paramNames{1}, ', ',paramNames{2}, ' and ',paramNames{3},displayString]));
xlabel(paramNames{1});
ylabel(paramNames{2});
zlabel(paramNames{3});
    
colorbar

view(50,50)
if(~strcmp(plotPath,''))
    saveas(gcf,plotPath,'png');
    saveas(gcf,plotPath,'fig');
end
end
