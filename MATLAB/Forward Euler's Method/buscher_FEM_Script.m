% Homework 7 Problem 3b
% Math 3340, Spring 2018

clear; close

dt = 0.01;
n = 15/dt;
t = linspace(0,15,n+1);
u0 = 0;
f = @(u,t) cos(t);

[ uN ] = FEM( u0, t, f, dt, n );

u = @(t) sin(t);

f1 = figure(1);
plot(t,u(t),'k')
hold on
plot(t, uN,'o','MarkerSize',8)
title('Forward Euler Method with Time Step of 0.01','FontSize',14)
xlabel('t'), ylabel('u(t)')
legend({'sin(t)','u(t)'},'FontSize',12)
saveas(f1, 'hw07problem03bPlot', 'epsc')