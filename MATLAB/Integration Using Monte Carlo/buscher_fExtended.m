function [ val ] = fExtended( x, y, z )
% This checks the point with coordinates
% (x,y,z) to see if it falls within the domain.
% If it does, the value of the function to be
% integrated is returned. Otherwise zero returned.
% So this is in fact an extension of the original
% function f defined on the domain D to the 
% rectangle R which contains D. 
f = @(x,y,z) 1; % Integral of 1 is the area.
val = 0;
if (x^2 + y^2 + y^2) < 1
    val = f(x,y,z);
end


end
