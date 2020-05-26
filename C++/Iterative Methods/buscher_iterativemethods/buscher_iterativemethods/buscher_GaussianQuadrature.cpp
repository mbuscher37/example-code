#include <iostream>
#include <vector>
#include "buscher_NumericalIntegration.h"

double NumericalIntegration::GaussianQuadrature(int n, double a, double b
	, double(*f)(double x))
{
	double h = (b - a) / n;
	vector<double> partition(n + 1);
	partition[0] = a;
	for (int i = 1; i <= n; i++)
	{
		partition[i] = partition[i - 1] + h;
	}
	
	//our x_i's for [-1,1]
	vector<double> X = { -0.5773502691896257645091488
		, 0.5773502691896257645091488 };

	//values to store our transformations
	double t_0;
	double t_1;

	double val = 0.0;
	for (int i = 0; i < n; i++)
	{
		t_0 = 0.5*partition[i] * (1 - X[0]) 
			+ 0.5*partition[i + 1] * (1 + X[0]);
		t_1 = 0.5*partition[i] * (1 - X[1]) 
			+ 0.5*partition[i + 1] * (1 + X[1]);
		val = val + (h / 2)*(f(t_0) + f(t_1));
	}
	return val;
}