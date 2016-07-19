
basePath='../../Experiments';

experiments_l1={'10000_50/0.5-1.0, 0.6-1.0','10000_100/0.3-1.0, 0.3-1.0',...
    '10000_100/0.5-1.0, 0.6-1.0', '50000_50/0.5-1.0, 0.6-1.0',...
    '50000_100/0.3-1.0, 0.3-1.0','50000_100/0.5-1.0, 0.5-1.0','50000_100/0.5-1.0, 0.5-1.0', '50000_100/0.5-1.0, 0.6-1.0' };

experiments_l1={'50000_100/0.5-1.0, 0.6-1.0'};

experiments_l2={'NS_Fixed','S_Fixed','NS_Var', 'S_Var'};

experiments_l3 = {'1','2','3','4'};

nContinuousParam=2;
nDiscreteParam=1;
objNames={'obj1','obj2'};

for i=1:length(experiments_l1)
    for j=1:length(experiments_l2)
        for k=1:length(experiments_l3)
            filepath=[basePath,'/',experiments_l1{i},'/',experiments_l2{j},'/',experiments_l3{k},'/'];
            paramFile = [filepath,'VAR_REGION_NSGAII'];
            objFile = [filepath,'FUN_REGION_NSGAII'];
            
            if ((exist(paramFile, 'file') == 2 ) && (exist(objFile, 'file') == 2 ))
                colors = 0;
                colorNames={'by_obj_space_vol'};
                if (strcmp(experiments_l2{j}(end-3:end),'_Var'))
                    colors = [1,4];
                    colorNames={'by_param_space_vol','by_sensitivity'};
                end
                for cl = 1:length(colors)
                    prefix=[experiments_l2{j},'_',experiments_l3{k},'_'];
                    plotFile = [filepath,prefix,colorNames{cl}];
                    [regions,volumes,paramRegions,paramVolumes] = front_2d(objFile, colors(cl), nContinuousParam, nDiscreteParam, ...
                        paramFile, objNames,plotFile);
                    if (strcmp(experiments_l2{j}(end-3:end),'_Var'))
                        front_2d_add_dimension(objFile, colors(cl), nContinuousParam, nDiscreteParam, ...
                            paramFile, 0, objNames,[plotFile,'_3rd_dim_param_vol']);
                        
                        front_2d_add_dimension(objFile, colors(cl), nContinuousParam, nDiscreteParam, ...
                            paramFile, 1, objNames,[plotFile,'_3rd_dim_sens']);
                    end
                end
                
            else
                disp([paramFile, ' or ',objFile, ' are missing.']);
            end
            close all
        end
    end
end
