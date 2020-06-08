function [ u ] = FEM( u0, t, f, dt, n )
% does forward euler method to approximate ODEs.
%   INPUT: u0 = initial condition, t = range of t values
%          f = given ODE, dt = time step
% OUTPUT: uN = approximate u(t)
u(1) = u0;
for i = 1:n
    u(i+1) = u(i) + dt*feval( f, u(i), t(i) );
    t(i+1) = t(i) + dt;
end
end

