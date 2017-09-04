
% - paramNames (optional). Default: {'param1','param2'}
% - alpha (optional). Opacity in plots. Default 0.5
% % - plotPath: path where to write the plot (default: '', i.e. just display,
% don't write)


function param_plot_2d(volumes,paramRegions,paramVolumes, paramNames, plotPath, alpha)

if(nargin < 6)
    alpha = 0.5;
    if(nargin < 5)
        plotPath='';
        if(nargin < 4)
            paramNames = {'param1','param2'};
        end
    end
end

sensitivities=volumes./paramVolumes;

figure
xdata=zeros(4,size(paramRegions,1));
ydata=zeros(4,size(paramRegions,1));
zdata=ones(4,size(paramRegions,1));

for i = 1:size(paramRegions,1)
    
    obj1m=paramRegions(i,1);
    obj1M=paramRegions(i,2);
    obj2m=paramRegions(i,3);
    obj2M=paramRegions(i,4);
    
    xdata(:,i) = [obj1m; obj1M; obj1M; obj1m];
    ydata(:,i) = [obj2m; obj2m; obj2M; obj2M];
    
end

colors = zeros(size(xdata));
for i=1:4
    colors(i,:)=sensitivities';
end
displayString = '. Colouring based on sensitivity.';

patch(xdata,ydata,zdata,colors,'FaceAlpha',alpha,'EdgeColor',[120 120 120]/255);

title(strcat(['Parameter space for ',paramNames{1}, ' and ',paramNames{2},displayString]));
xlabel(paramNames{1});
ylabel(paramNames{2});

colorbar

if(~strcmp(plotPath,''))
    saveas(gcf,plotPath,'png');
    saveas(gcf,plotPath,'fig');
end
end
