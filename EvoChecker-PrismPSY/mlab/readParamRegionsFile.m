function [regions,volumes] = readParamRegionsFile(regionsFilePath, nContinousParams, nDiscreteParams)

formatSpec = '';
len = 0;
for i=1:nContinousParams
    formatSpec=[formatSpec,'%f:%f,'];
    len = len + 2;
end
for i=1:nDiscreteParams
    formatSpec=[formatSpec,'%d '];
    len = len + 1;
end
sizeA = [len Inf];

fileID = fopen(regionsFilePath,'r');

regions = fscanf(fileID,formatSpec,sizeA);
regions=regions';
%add volume
volumes = ones(size(regions,1),1);
for i = 1:nContinousParams
    volumes = volumes.*(regions(:,2*i)-regions(:,2*i-1));
end
fclose(fileID);