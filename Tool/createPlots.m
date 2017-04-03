
% - regionsObjPath: path to the file containing the
% regions in the objective space. The format has to be:
% obj1_min:obj1_max,obj2_min:obj2_max,obj3_min:obj3_max,
% - objNames (optional). Default: {'obj1','obj2'}
% - plotPath: path where to write the plot (default: '', i.e. just display,
% don't write)
% - alpha (optional). Opacity in plots. Default 0.5

%function [dataPath, experiment, continusParams, discreteParms, objetives] = 
function createPlots(dataPath, experimentName, nContinuousParam, nDiscreteParam, nObjectives)

FUN = strcat(dataPath, 'FUN_REGION_', experimentName);
VAR = strcat(dataPath, 'VAR_REGION_', experimentName);

fprintf('%s\n', FUN);
fprintf('%s\n', VAR);
plot_regions (FUN, nObjectives, nContinuousParam, nDiscreteParam, VAR, experimentName);

end
