#include <vector>

using namespace std;
class NumericalIntegration 
{
public:
	double CompositeTrapezoidal(int n, double a, double b
		, double (*f)(double x));
	double GaussianQuadrature(int n, double a, double b
		, double(*f)(double x));
	double CompositeMidpoint(int n, double a, double b
		, double(*f)(double x));
	double CompositeMidpoint(int n, double a, double b
		, double(*f)(double t, double x), double x);
};

