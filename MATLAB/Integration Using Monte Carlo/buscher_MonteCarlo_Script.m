% Homework 7 Problem 2
% Math 3340, Spring 2018
% Written by Meghan Buscher

clear

a = -1;
b = 1;

M = 100000;

xyz = a + (b-a)*rand(3,M);

s = 0;
for i = 1:M
    s = s + fExtended( xyz(1,i), xyz(2,i), xyz(3,i) );
end
% average value of the extended function.
fbar = s / M;

I = 8 * fbar;

fprintf('Volume of Unit Sphere Using Monte Carlo\n')
fprintf('Volume = %f \n', I)
fprintf('Error = %f \n', abs(((4/3)*pi)-I))

function [ val ] = fExtended( x, y, z )
% This checks the point with coordinates
% (x,y,z) to see if it falls within the domain.
% If it does, the value of the function to be
% integrated is returned. Otherwise zero returned.
% So this is in fact an extension of the original
% function f defined on the domain D to the 
% rectangle R which contains D. 
f = @(x,y,z) 1;
val = 0;
if (x^2 + y^2 + z^2) <= 1
    val = f(x,y,z);
end

end
