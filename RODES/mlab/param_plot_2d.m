
% - regionsObjPath: path to the file containing the
% regions in the objective space. The format has to be:
% obj1_min:obj1_max,obj2_min:obj2_max,obj3_min:obj3_max,
% - objNames (optional). Default: {'obj1','obj2'}
% - transparency (optional). Transparency in plots. Default 0.5
% - colouring (optional). 
%   colouring=0, colours each box based on the volume of the region (obj
%   space)
%   colouring = 1, colours each box based on the volume of the region
%   in the param space. Needs to specify the path of the VAR_REGION file
%   and the number of continuous and discrete parameters
%   colouring = 2, colours each box with a random color.
%   colouring = 3, uses a gradient over the obj space
%   colouring = 4, colours each box based on the sensitivity. Needs to specify the path of the VAR_REGION file
% - plotPath: path where to write the plot (default: '', i.e. just display,
% don't write)


function param_plot_2d(regions,volumes,paramRegions,paramVolumes, colouring, paramNames, plotPath, transparency)

if(nargin < 8)
    transparency = 0.5;
    if(nargin < 7)
        plotPath='';
        if(nargin < 6)
            paramNames = {'param1','param2'};
                if(nargin < 5)
                    colouring=2;
                end
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
switch colouring
    case 0
        for i=1:4
            colors(i,:)=volumes';
        end
        displayString = '. Colouring based on obj space volume.';
    case 1
        for i=1:4
            colors(i,:)=paramVolumes';
        end
        displayString = '. Colouring based on param space volume.';
    case 2
        tmpcolors = rand([1,size(xdata,2)]);
        for i=1:4
            colors(i,:)=tmpcolors;
        end
        displayString = '. Random colouring.';
    case 3
        displayString = '. Gradient on objective space.';
        o1m=min(regions(1,:));
        o1M=max(regions(2,:));
        o2m=min(regions(3,:));
        o2M=max(regions(4,:));
        
        mones=ones(size(xdata));
        colors = (xdata - o1m*mones)./(o1M-o1m) + (ydata - o2m*mones)./(o2M-o2m);
        
    otherwise
        for i=1:4
            colors(i,:)=sensitivities';
        end
        displayString = '. Colouring based on sensitivity.';     
        
end

patch(xdata,ydata,zdata,colors,'FaceAlpha',transparency,'EdgeColor',[120 120 120]/255);

title(strcat(['Parameter space for ',paramNames{1}, ' and ',paramNames{2},displayString]));
xlabel(paramNames{1});
ylabel(paramNames{2});
if(colouring<2 || colouring>3)
    colorbar
end
if(~strcmp(plotPath,''))
    saveas(gcf,plotPath,'png');
    saveas(gcf,plotPath,'fig');
end
end
