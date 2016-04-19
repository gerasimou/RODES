function regions = readRegionsFile(regionsFilePath, nObj)
if(nargin<2)
    nObj=2;
end
fileID = fopen(regionsFilePath,'r');

if(nObj<3)
    formatSpec = '%f:%f,%f:%f,';
    sizeA = [4 Inf];
else
    formatSpec = '%f:%f,%f:%f,%f:%f,';
    sizeA = [6 Inf];
end

regions = fscanf(fileID,formatSpec,sizeA);
regions=regions';
fclose(fileID);