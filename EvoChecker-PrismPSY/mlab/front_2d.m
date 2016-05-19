
% - regionsFilePath: path to the file containing the
% regions in the objective space. The format has to be:
% obj1_min:obj1_max,obj2_min:obj2_max,obj3_min:obj3_max,
% - objNames (optional). Default: ['p1';'p2';'p3']
% - transparency (optional). Transparency in plots. Default 0.5
% - randCols (optional). If true, colours each box with a random color. If
% false, it uses a gradient over the obj space

function front_2d(regionsFilePath, randCols, objNames, transparency)

if(nargin < 4)
    transparency = 0.5;
    if(nargin < 3)
        objNames = {'obj1','obj2'};        
        if(nargin < 2)
            randCols = true;
        end
    end
    
end

regions = readRegionsFile(regionsFilePath,2);

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


if randCols
        tmpcolors = rand([1,size(xdata,2)]);
        colors = zeros(size(xdata));
        for i=1:4
            colors(i,:)=tmpcolors;
        end
else
    
    o1m=min(regions(1,:));
    o1M=max(regions(2,:));
    o2m=min(regions(3,:));
    o2M=max(regions(4,:));
    
    mones=ones(size(xdata));
    colors = (xdata - o1m*mones)./(o1M-o1m) + (ydata - o2m*mones)./(o2M-o2m);
    
end

patch(xdata,ydata,zdata,colors,'FaceAlpha',transparency,'EdgeColor',[120 120 120]/255)

title(strcat(['Pareto Front for objectives ',objNames{1}, ' and ',objNames{2}]));
xlabel(objNames{1});
ylabel(objNames{2});

end
