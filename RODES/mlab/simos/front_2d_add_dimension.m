
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
%   depthBySensitivity. If = 0 (default) the depth of each box is
%   proportional to the volume in the parameter space. If = 1, proportional
%   to the sensitivity
% - plotPath: path where to write the plot (default: '', i.e. just display,
% don't write)

function [regions,volumes,paramRegions,paramVolumes] = front_2d_add_dimension(regionsObjPath, colouring, nContinuousParam, nDiscreteParam, ...
    regionsParamPath, depthBySensitivity, objNames, plotPath, transparency)

if(nargin < 9)
    transparency = 0.5;
    if(nargin < 8)
        plotPath='';
        if(nargin < 7)
            objNames = {'obj1','obj2'};
            if(nargin < 6)
                depthBySensitivity=0;
                if(nargin < 5)
                    regionsParamPath = '';
                    if(nargin < 2 || (nargin>1 && (colouring==1||colouring==4)))
                        colouring=2;
                    end
                end
            end
            
        end
    end
end

[regions,volumes] = readRegionsFile(regionsObjPath,2);
[paramRegions,paramVolumes] = readParamRegionsFile(regionsParamPath,nContinuousParam,nDiscreteParam);
sensitivities=volumes./paramVolumes;

if(depthBySensitivity==0)
    zlbl='Param space vol';
    trd_dim=paramVolumes;
else
    zlbl='Sensitivity';
    trd_dim=sensitivities;
end

figure
xdata=zeros(4,size(regions,1));
ydata=zeros(4,size(regions,1));
zdata=ones(4,size(regions,1));

for i = 1:size(regions,1)
    obj1m=regions(i,1);
    obj1M=regions(i,2);
    obj2m=regions(i,3);
    obj2M=regions(i,4);
    obj3m=0;
    obj3M=trd_dim(i);
    
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

title(strcat(['Pareto Front for objectives ',objNames{1}, ' and ',objNames{2},displayString]));
xlabel(objNames{1});
ylabel(objNames{2});
zlabel(zlbl);
if(colouring<2 || colouring>3)
    colorbar
end
view(50,50)
if(~strcmp(plotPath,''))
    saveas(gcf,plotPath,'png');
    saveas(gcf,plotPath,'fig');
end
end
