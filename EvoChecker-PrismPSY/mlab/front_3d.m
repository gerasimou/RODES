
% - regionsFilePath: path to the file containing the
% regions in the objective space. The format has to be:
% obj1_min:obj1_max,obj2_min:obj2_max,obj3_min:obj3_max,
% - objNames (optional). Default: ['p1';'p2';'p3']
% - transparency (optional). Transparency in plots. Default 0.5
% - randCols (optional). If true, colours each box with a random color. If
% false, it uses a gradient over the obj space

function front_3d(regionsFilePath, randCols, objNames, transparency)

if(nargin < 4)
    transparency = 0.5;
    if(nargin < 3)
        objNames = {'obj1','obj2','obj3'};        
        if(nargin < 2)
            randCols = true;
        end
    end
    
end



regions = readRegionsFile(regionsFilePath,3);


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


if randCols
    
    randcolors = rand([size(regions,1),1]);
    colors = zeros(size(xdata));
    
    for i=1:size(colors,1)
        for j=1:size(xdata,2)
            colors(i,j)=randcolors(ceil(j./6));
        end
    end
else
    o1m=min(regions(1,:));
    o1M=max(regions(2,:));
    o2m=min(regions(3,:));
    o2M=max(regions(4,:));
    o3m=min(regions(5,:));
    o3M=max(regions(6,:));
    
    mones=ones(size(xdata));
    colors = (xdata - o1m*mones)./(o1M-o1m) + (ydata - o2m*mones)./(o2M-o2m) + (zdata - o3m*mones)./(o3M-o3m);
    
end

patch(xdata,ydata,zdata,colors,'FaceAlpha',transparency,'EdgeColor',[120 120 120]/255)

title(strcat(['Pareto Front for objectives ',objNames{1}, ', ',objNames{2}, ' and ',objNames{3}]));
xlabel(objNames{1});
ylabel(objNames{2});
zlabel(objNames{3});

end
