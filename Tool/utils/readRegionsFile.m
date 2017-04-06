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

%fprintf('\nfileID %d, format %s, size %d', fileID,formatSpec,sizeA);
%fprintf('\nfile %s', regionsFilePath);

regions = fscanf(fileID,formatSpec,sizeA);
regions=regions';
regions = regions(:,1:nObj*2);
fclose(fileID);
volumes = ones(size(regions,1),1);
for i = 1:nObj
    volumes=volumes.*(regions(:,i*2)-regions(:,i*2-1));
end
