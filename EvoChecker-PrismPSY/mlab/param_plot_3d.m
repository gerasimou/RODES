
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


function param_plot_3d(regions,volumes,paramRegions,paramVolumes, colouring, paramNames, plotPath, transparency)

if(nargin < 8)
    transparency = 0.5;
    if(nargin < 7)
        plotPath='';
        if(nargin < 6)
            paramNames = {'param1','param2','param3'};
                if(nargin < 5)
                    colouring=2;
                end
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
switch colouring
    case 0
        for i=1:size(colors,1)
            for j=1:size(xdata,2)
                colors(i,j)=volumes(ceil(j./6));
            end
        end
        displayString = '. Colouring based on obj space volume.';
    case 1
        for i=1:size(colors,1)
            for j=1:size(xdata,2)
                colors(i,j)=paramVolumes(ceil(j./6));
            end
        end
        displayString = '. Colouring based on param space volume.';
    case 2
        randcolors = rand([size(regions,1),1]);
        
        for i=1:size(colors,1)
            for j=1:size(xdata,2)
                colors(i,j)=randcolors(ceil(j./6));
            end
        end
        
        displayString = '. Random colouring.';
    case 3
        displayString = '. Gradient on objective space.';
        o1m=min(regions(1,:));
        o1M=max(regions(2,:));
        o2m=min(regions(3,:));
        o2M=max(regions(4,:));
        o3m=min(regions(5,:));
        o3M=max(regions(6,:));
        
        mones=ones(size(xdata));
        colors = (xdata - o1m*mones)./(o1M-o1m) + (ydata - o2m*mones)./(o2M-o2m) + (zdata - o3m*mones)./(o3M-o3m);
        
    otherwise
        for i=1:size(colors,1)
            for j=1:size(xdata,2)
                colors(i,j)=sensitivities(ceil(j./6));
            end
        end
        displayString = '. Colouring based on sensitivity.';     
        
end


patch(xdata,ydata,zdata,colors,'FaceAlpha',transparency,'EdgeColor',[120 120 120]/255)

title(strcat(['Parameter space for ',paramNames{1}, ', ',paramNames{2}, ' and ',paramNames{3},displayString]));
xlabel(paramNames{1});
ylabel(paramNames{2});
zlabel(paramNames{3});
if(colouring<2)
    colorbar
end
view(50,50)
if(~strcmp(plotPath,''))
    saveas(gcf,plotPath,'png');
    saveas(gcf,plotPath,'fig');
end
end
