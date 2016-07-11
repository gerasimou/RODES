function [regions,volumes] = readRegionsFile(regionsFilePath, nObj)
if(nargin<2)
    nObj=2;
end
fileID = fopen(regionsFilePath,'r');

if(nObj<3)
    formatSpec = '%f:%f,%f:%f,%f';
    sizeA = [5 Inf];
else
    formatSpec = '%f:%f,%f:%f,%f:%f,%f';
    sizeA = [7 Inf];
end

regions = fscanf(fileID,formatSpec,sizeA);
regions=regions';
volumes = regions(:,nObj*2+1);
regions = regions(:,1:nObj*2);
fclose(fileID);