#include <iostream>
#include <vector>
#include <iomanip>
#include <cmath>

using namespace std;

class IterMethod
{
private:
	int n;
	double h;
	vector<double> t;

public:
	IterMethod(int N, double a, double b);
	IterMethod(int N);
	vector<double> getPartition();
	vector<double> Newtons3D(double(*f)(double x, double y), double(*dfdy)(double x, double y));
	vector<vector<double>> NewtonsND(double alpha, vector<double>(*F)(vector<double> x), tridiagonal_matrix(*derivF)(vector<double> x));
	vector<double> GaussSeidel(double(*f)(double x), double(*r)(double x));
	vector<vector<double>> getAlpha();
	double MatrixVectorMultGS(int i, vector<double> v, vector<double> w);
	vector<double> LinearIterMethod(double alpha, double(*f)(double x), double(*r)(double x));
	vector<double> getRHS(double alpha, vector<double> U, double(*f)(double x));
	tridiagonal_matrix getMatrix(double alpha, double(*r)(double x));
};
