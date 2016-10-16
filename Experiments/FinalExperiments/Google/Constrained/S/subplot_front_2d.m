
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
%   colouring = 5, fixed colour
% - plotPath: path where to write the plot (default: '', i.e. just display,
% don't write)


function [regions,volumes,paramRegions,paramVolumes] = front_2d( ...
experiment, variant, colouring, nContinuousParam, nDiscreteParam, ...
objNames, transparency)

%regionsObjPath, colouring, nContinuousParam, nDiscreteParam, ...
%regionsParamPath, objNames, plotPath, transparency)

% e.g., front_2d('50000_50/0.5-1.0, 0.6-1.0', 'S_Var/2',1,2,1)

base = '/Users/sgerasimou/Documents/Git/search-based-model-synthesis/EvoChecker-PrismPSY/data/';
base = '/Users/sgerasimou/Documents/Git/search-based-model-synthesis/Experiments/';


regionsObjPath      = strcat(base, experiment, '/FUN_REGION_', variant);
regionsParamPath    = strcat(base, experiment, '/VAR_REGION_', variant);
%regionsObjPath      = strcat(base, experiment, '/', variant,'/FUN_REGION_NSGAII');
%regionsParamPath    = strcat(base, experiment, '/', variant,'/VAR_REGION_NSGAII');

disp(regionsObjPath);

if(nargin < 8)
    transparency = 0.9;
    if(nargin < 7)
        plotPath='';
        if(nargin < 6)
            objNames = {'obj1 (max)','obj2(max)'};
            if(nargin < 5)
                regionsParamPath = '';
                if(nargin < 2 || (nargin>1 && (colouring==1||colouring==4)))
                    colouring=2;
                end
            end
        end
    end
end


[regions,volumes] = readRegionsFile(regionsObjPath,2);
[paramRegions,paramVolumes] = readParamRegionsFile(regionsParamPath,nContinuousParam,nDiscreteParam);
sensitivities=volumes./paramVolumes;

subplots = 9;
figure
for subplotIndex = 1:subplots
    subplot(3,3,subplotIndex)
    xdata=zeros(4,size(regions,1)/subplots);
    ydata=zeros(4,size(regions,1)/subplots);
    zdata=ones(4,size(regions,1)/subplots);

    start = (subplotIndex-1)*20+1;
    stop  = subplotIndex*20;
    %for i = 1:size(regions,1)
    i = 1;
    for index = start:stop

        obj1m=regions(index,1);
        obj1M=regions(index,2);
        obj2m=regions(index,3);
        obj2M=regions(index,4);

        xdata(:,i) = [obj1m; obj1M; obj1M; obj1m];
        ydata(:,i) = [obj2m; obj2m; obj2M; obj2M];
        i = i+1;
    end

    colors = zeros(size(xdata));
    switch colouring
        case 0
            for i=1:4
                colors(i,:)=volumes(start:stop);
            end
        case 1
            for i=1:4
                colors(i,:)=paramVolumes';
            end
        case 3
            o1m=min(regions(1,:));
            o1M=max(regions(2,:));
            o2m=min(regions(3,:));
            o2M=max(regions(4,:));

            mones=ones(size(xdata));
            colors = (xdata - o1m*mones)./(o1M-o1m) + (ydata - o2m*mones)./(o2M-o2m);

        case 4
            for i=1:4
                colors(i,:)=sensitivities';
            end
        case 5
            population = 20;
            tmpcolors = [1,2,3,0.1]; %[0.1, 0.4, 0.7, 1];%rand([1,size(xdata,2)]);
            j =1;
            for i = 1:population:size(colors,2)
                colors(:,i:i+population-1)= tmpcolors(j);
                j = j+1;
            end
        otherwise
            colors = 0.5*ones(size(xdata));
            displayString = '. Fixed colour.';   
    end

    patch(xdata,ydata,zdata,colors,'FaceAlpha',transparency,'EdgeColor',[120 120 120]/255);
end


%title(strcat(['Pareto Front for objectives ',objNames{1}, ' and ',objNames{2},displayString]));

%text(min(regions(:,2)),max(regions(:,4)),strcat('Experiment: ', experiment, ' _  ', variant), 'Interpreter','none');

subplot(3,3,4)
ylabel('Expected time the system is in a cost-optimal state during the first 60 hours');%objNames{2});
subplot(3,3,8)
xlabel('Probability of recovery SL1 in the interval [10,60]hours');%(objNames{1});

%if(colouring<2 || colouring>3)
%    colorbar
%end

%if(~strcmp(plotPath,''))
%    saveas(gcf,plotPath,'png');
%    saveas(gcf,plotPath,'fig');
%end

fileName = strcat(variant(1:length(variant)));
disp(fileName)
saveas(gcf,strcat(base, experiment, '/', fileName),'pdf')
saveas(gcf,strcat(base, experiment, '/', fileName),'svg')
end
