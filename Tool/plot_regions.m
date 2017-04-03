% - regionsObjPath: path to the file containing the regions in the objective space.
% - nObjs: number of objectives
% - nContinuousParam: number of continuous parameters
% - nDiscreteParam: number of discrete parameters
% - regionsObjPath: path to the file containing the synthesised regions in the parameter space.
% - objNames (optional). Default: {'obj1','obj2','obj3'}
% - paramNames (optional). Default: {'param1','param2','param3'}
% - alpha (optional). Opacity in plots. Default 0.5. Octave does not support transparency and thus has alpha=1
% - experimentName: experiment name, optional (default: 'rob_synth')

function plot_regions(regionsObjPath,n_objs,nContinuousParam,nDiscreteParam,regionsParamPath,experimentName, objNames, paramNames,alpha)
    addpath('utils');
    if(nargin < 9)
        alpha = 0.5;
        if(nargin < 8)
            paramNames = {'param1','param2','param3'};
            if(nargin < 7)
                objNames = {'obj1','obj2','obj3'};
                if(nargin < 6)
                    experimentName='rob_synth';
                end
            end
        end
    end

    isOctave = exist('OCTAVE_VERSION', 'builtin') ~= 0;
    % Octave doesn't support patch transparency
    if isOctave
        alpha=1;
    end
    
    
    %check if graphs dir exists
    graphDir = exist('graphs', 'dir');
    fprintf('%d', graphDir);
    if (graphDir ~= 7)
        mkdir('graphs');
    end 
   

    fprintf('Plotting for %d objectives\n', n_objs);
    if n_objs==2
        % plot 2d objective space
        [~,volumes,paramRegions,paramVolumes,meanVol,meanSens,maxSens] = ...
            front_2d(regionsObjPath, nContinuousParam, nDiscreteParam,regionsParamPath,...
            objNames(1:2), ['./graphs/', experimentName,'_objs'],alpha);

    elseif n_objs==3
        % plot 3d objective space
        [~,volumes,paramRegions,paramVolumes,meanVol,meanSens,maxSens] = ...
            front_3d(regionsObjPath, nContinuousParam, nDiscreteParam,regionsParamPath,...
            objNames, ['./graphs/', experimentName,'_objs'],alpha);
    else
        error('Plotting is only supported for 2 and 3 objectives');
    end

    fprintf('Mean volume of Pareto regions: %f\n',meanVol);
    fprintf('Mean sensitivity of Pareto regions: %f\n',meanSens);
    fprintf('Max sensitivity of Pareto regions: %f\n',maxSens);

    nParams=nDiscreteParam+nContinuousParam;

    %need to add columns to the parameter regions var in order to show also
    %discrete variables

    discreteP_columns=zeros(size(paramRegions,1),2*nDiscreteParam); 
    for i=1:nDiscreteParam
        discreteP_columns(:,2*i) = paramRegions(:,end-nDiscreteParam+i);
    end

    paramRegions_1=[paramRegions(:,1:end-nDiscreteParam),discreteP_columns];


    if nParams == 2
        param_plot_2d(volumes,paramRegions_1,paramVolumes,paramNames(1:2),['./graphs/', experimentName,'_params'],alpha);
    elseif nParams == 3
        param_plot_3d(volumes,paramRegions_1,paramVolumes,paramNames,['./graphs/', experimentName,'_params'],alpha);
    else
        disp('Plotting of parameter regions is supported only for 2 or 3 parameters');
    end

end