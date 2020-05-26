#include <iostream>
#include <fstream>
#include <iomanip>
#include <vector>
#include "buscher_interpolatingpolynomial.h"
#include "buscher_NumericalIntegration.h"
#include "buscher_polynomial.h"
#include "buscher_tridiagonal_matrix.h"
#include "buscher_IterMethod.h"


double myfunc(double x, double y)
//Function needed for problems 1 & 2
{
	return (x + y)*cos(y) + exp(-20.0 * x*x*x - y * y*y) - 1.0 / (x*x + y * y + 1);
}

double partderivY(double x, double y)
//Function needed for problems 1 & 2
{
	double val = cos(y) - (x + y)*sin(y) - 3.0*y*y*exp(-20.0 * x*x*x - y * y*y);
	return val + (2.0*y) / (pow(x*x + y * y + 1, 2));
}

vector<double> Func(vector<double> x)
//Function needed for problem 3
{
	int n = x.size() - 1;
	vector<double> F(n + 1);
	F[0] = -400.0*x[0] * (x[1] - x[0] * x[0]);
	for (int i = 1; i < n; i++)
	{
		F[i] = 200.0*(x[i] - x[i - 1] * x[i - 1]) - 2.0*(1.0 - x[i]);
		F[i] += -400.0*x[i] * (x[i + 1] - x[i] * x[i]);
	}
	F[n] = 200.0*(x[n] - x[n - 1] * x[n - 1]) - 2.0*(1.0 - x[n]);
	
	return F;
}

tridiagonal_matrix FuncDeriv(vector<double> x)
//Function needed for problem 3
{
	int n = x.size() - 1;
	tridiagonal_matrix tdmat(n + 1);

	double val;
	for (int i = 0; i < n; i++)
	{
		val = -400.0*x[0];
		tdmat.set_upper_diagonal_entry(i, val);
		tdmat.set_lower_diagonal_entry(i, val);
	}

	double diag = 800.0*x[0] * x[0] - 400.0*(x[1] - x[0] * x[0]);
	tdmat.set_diagonal_entry(0, diag);
	for (int i = 1; i < n; i++)
	{
		diag = 200.0*x[i] + 2.0 + 800.0*x[i] * x[i];
		diag += -400 * (x[i + 1] - x[i] * x[i]);
		tdmat.set_diagonal_entry(i, diag);
	}
	diag = 200.0*x[n] + 2;
	tdmat.set_diagonal_entry(n, diag);
	return tdmat;
}

double u(double x)
//Function needed for problem 4
{
	/*double C = (cosh(1.0) - 1.0) / sinh(1.0);
	return -cosh(x) + C * sinh(x) + 1.0;*/
	double pi = 4.0*atan(1.0);
	return (x / 5.0)*(sin(1.0 / x) - (sin(1.0 / pi)*sinh((2.0 / x) - 2.0 * pi) / sinh((2.0 / pi) - 2.0 * pi)));
}

double f(double x)
//Function needed for problems 4, 5, & 6
{
	//return 1.0;
	return (1.0 / (x*x*x))*sin(1.0 / x);
	//return fabs(x); //needed for problem 6
}

double r(double x)
//Function needed for problems 4 & 5
{
	//return 1.0;
	return 4.0 / (x*x*x*x);
}

double func(double t, double x)
//Function needed for problem 6
{
	return exp(t) / (exp(2.0*t) + x * x);
}

double R_h(int n, double h, double x)
//Function needed for problem 6
{
	double pi = 4.0*atan(1.0);
	double val = 2.0*x*x / pi;

	NumericalIntegration NI;
	double a = -n * h / 2.0;
	double b = n * h / 2.0;
	return val * (NI.CompositeMidpoint(n, a, b, func, x));
}

int main()
{
	/* ---- code needed for Problems 1 & 2 ---- */
	/*vector<int> n = { 8, 16, 32, 64, 128, 256 };
	double a = 0;
	double b = 5;
	//ofstream output("problem1.txt");
	ofstream output("problem2.txt");
	IterMethod * itermet;
	for (int j = 0; j < n.size(); j++)
	{
		itermet = new IterMethod(n[j], a, b);
		vector<double> t = itermet->getPartition();
		vector<double> r = itermet->Newtons3D(myfunc, partderivY);

		vector<boundary_type> type(2);
		vector<double> val(2);
		type[0] = boundary_type::second_derivative;
		type[1] = boundary_type::second_derivative;
		val[0] = 0.0;
		val[1] = 0.0;
		vector<vector<double>> data(n[j] + 1, vector<double>(2));
		for (int i = 0; i <= n[j]; i++)
		{
			data[i][0] = t[i];
			data[i][1] = r[i];
		}

		InterpolatingPolynomial * P = new CubicSpline(data, type, val);

		int nres = 100;
		vector<vector<double>> coord = P->getCurveCoordinates(nres);

		//for (int i = 0; i <= n[j]; i++)
		for (int i = 0; i < coord.size(); i++)
		{
			//output << n[j] << " " << t[i] << " " << r[i] << endl;
			output << n[j] << " " << coord[i][0] << " " << coord[i][1] << endl;
		}
	}
	
	output.close();*/
	/* ---------------------------------------- */

	/* ------- code needed for Problem 3 ------ */
	//vector<int> n = { 9, 19, 39, 79, 99 };
	/*int n = 99;
	double alpha = 1.0;
	IterMethod * itermet;
	//for (int j = 0; j < n.size(); j++)
	//{
	itermet = new IterMethod(n);
	vector<vector<double>> x = itermet->NewtonsND(alpha, Func, FuncDeriv);

	ofstream output("problem3.txt");
	double val1, val2;
	double diff1, diff2;
	for (int m = 1; m < x.size(); m++)
	{
		val1 = fabs(1.0 - x[m][0]);
		val2 = fabs(x[m][0] - x[m - 1][0]);
		for (int i = 1; i < x[m].size(); i++)
		{
			diff1 = fabs(1.0 - x[m][i]);
			diff2 = fabs(x[m][i] - x[m - 1][i]);
			if (diff1 > val1)
			{
				val1 = diff1;
			}
			if (diff2 > val2)
			{
				val2 = diff2;
			}
		}
		output << m << " " << val1/val2 << endl;
	}
	output.close();*/
	/* ---------------------------------------- */

	/* ---- code needed for Problems 4 & 5 ---- */
	vector<int> n = { 16, 32, 64, 128, 256 };
	/*double a = 0.0;
	double b = 1.0;*/
	double pi = 4.0*atan(1.0);
	double a = 1.0 / pi;
	double b = pi;
	double h;
	double alpha = 1.0;

	IterMethod * itermet;
	vector<double> x, U;
	double val = 0, diff;
	//ofstream output("problem4.txt");
	ofstream output("problem5.txt");
	for (int i = 0; i < n.size(); i++)
	{
		itermet = new IterMethod(n[i], a, b);
		//U = itermet->GaussSeidel(f, r);
		U = itermet->LinearIterMethod(alpha, f, r);

		h = (b - a) / n[i];
		x = itermet->getPartition();
		
		for (int j = 0; j <= n[i]; j++)
		{
			diff = fabs(u(x[j]) - U[j]);
			if (diff > val)
			{
				val = diff;
			}
		}
		cout << "for n = " << n[i] << " h = " << h << " and val = " << val << endl;
		output << n[i] << " " << h << " " << val << endl;
	} 
	system("pause");
	output.close();
	/* ---------------------------------------- */

	/* ------- code needed for Problem 6 ------ */
	/* ------------ Part 1 ------------ */
	/*vector<int> n = { 2, 4, 8, 16, 32, 64 };
	double a = -1.0;
	double b = 1.0;
	double h;
	ofstream output("problem6.txt");
	for (int j = 0; j < n.size(); j++)
	{
		h = (b - a) / n[j];
		vector<vector<double>> data(n[j] + 1, vector<double>(2));
		data[0][0] = a;
		data[0][1] = f(a);
		for (int i = 1; i <= n[j]; i++)
		{
			data[i][0] = data[i - 1][0] + h;
			data[i][1] = f(data[i][0]);
		}

		vector<boundary_type> type(2);
		vector<double> val(2);
		type[0] = boundary_type::second_derivative;
		type[1] = boundary_type::second_derivative;
		val[0] = 0.0;
		val[1] = 0.0;

		InterpolatingPolynomial * P = new CubicSpline(data, type, val);

		int nres = 1000;
		vector<vector<double>> coord = P->getCurveCoordinates(nres);

		double max = 0.0, diff;
		for (int i = 0; i < coord.size(); i++)
		{
			//output << n[j] << " " << coord[i][0] << " " << f(coord[i][0]) << " " << coord[i][1] << endl;
			diff = fabs(f(coord[i][0]) - coord[i][1]);
			if (diff > max)
			{
				max = diff;
			}
		}
		cout << "Infinity norm for n = " << n[j] << " using a Cubic Spline is " << max << endl;
		output << n[j] << " " << h << " " << max << endl;
	}
	system("pause");
	output.close();*/
	/* -------------------------------- */

	/* ------------ Part 2 ------------ */
	/*vector<int> n = { 2, 4, 8, 16, 32, 64 };
	double pi = 4.0*atan(1.0);
	double max, diff;
	ofstream output("problem6.txt");
	for (int j = 0; j < n.size(); j++)
	{
		double h = pi * sqrt(2.0 / n[j]);
		vector<double> x(n[j] + 1);
		x[0] = -1.0 * n[j] * h / 2.0;
		for (int i = 1; i <= n[j]; i++)
		{
			x[i] = x[i - 1] + h;
		}

		max = 0.0;
		for (int i = 0; i <= n[j]; i++)
		{
			output << n[j] << " " << x[i] << " " << f(x[i]) << " " << R_h(n[j], h, x[i]) << endl;
			diff = fabs(f(x[i]) - R_h(n[j], h, x[i]));
			if (diff > max)
			{
				max = diff;
			}
		}
		//output << n[j] << " " << h << " " << max << endl;
		cout << "Infinity norm for n = " << n[j] << " Composite Midpoint Rule is " << max << endl;
	}
	system("pause");
	output.close();*/
	/* -------------------------------- */
	/* ---------------------------------------- */
}