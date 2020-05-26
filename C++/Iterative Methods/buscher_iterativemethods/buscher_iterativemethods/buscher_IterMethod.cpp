#include <iostream>
#include <vector>
#include <cmath>
#include "buscher_tridiagonal_matrix.h"
#include "buscher_IterMethod.h"

IterMethod::IterMethod(int N, double a, double b)
{
	n = N;
	h = (b - a) / n;
	t.resize(n + 1);
	t[0] = a;
	for (int i = 1; i <= n; i++)
	{
		t[i] = t[i - 1] + h;
	}
}

IterMethod::IterMethod(int N)
{
	n = N;
}

vector<double> IterMethod::getPartition()
{
	return t;
}

vector<double> IterMethod::Newtons3D(double(*f)(double x, double y), double(*dfdy)(double x, double y))
{
	/*This method performs Newton's iterative method to find the value r such that
	  f(r,t) = 0 for a fixed t. */
	double TOL = pow(10.0, -7);
	int maxIters = 1000;
	bool converged;
	vector<double> r(n + 1);
	r[0] = 0;

	double rm, diff;
	int m;
	for (int i = 1; i <= n; i++)
	{
		converged = false;
		m = 1;
		r[i] = r[i - 1];
		rm = r[i];
		while (!converged && m < maxIters)
		{
			r[i] = rm - f(t[i], rm) / dfdy(t[i], rm);
			diff = fabs(r[i] - rm);
			if (diff < TOL)
			{
				converged = true;
				cout << "Convergence reached in " << m << " iterations" << endl;
			}
			else
			{
				m++;
				rm = r[i];
			}
		}
		if (!converged && m >= maxIters)
		{
			cout << "Convergence not reached." << endl;
		}
	}
	system("pause");
	return r;
}

vector<vector<double>> IterMethod::NewtonsND(double alpha, vector<double>(*F)(vector<double> x), tridiagonal_matrix(*derivF)(vector<double> x))
{
	/*This method performs Newtons iterative method for finding zeros of a given
	  function F whose inputs and outputs are vectors in R^{n+1} and whose
	  derivative, derivF, is a tridiagonal matrix. */
	int maxIters = 1000;
	double TOL = pow(10.0, -20);
	bool converged = false;

	//2-d vector to hold our vectors at each iteration level
	vector<vector<double>> x(2, vector<double>(n + 1));

	//initial guess
	for (int i = 0; i <= n; i++)
	{
		x[0][i] = (i + 1.0) / (n + 2.0);
	}

	int m = 1;
	//holder vector to handle the right side of the equation
	vector<double> rhs(n + 1);
	//tridiagonal matrix to hold our derivative
	tridiagonal_matrix tdmat(n + 1);
	//solution of our linear system
	vector<double> h(n + 1);
	//values needed for finding infinity norms
	double diff1, diff2;
	double val1, val2;
	while (!converged && m < maxIters)
	{
		for (int i = 0; i <= n; i++)
		{
			rhs[i] = -1.0*F(x[m - 1])[i];
		}

		//Calculate F'(x^(m-1)) + alpha*I
		tdmat = derivF(x[m - 1]);
		double diag;
		for (int i = 0; i <= n; i++)
		{
			diag = tdmat.get_diagonal_entry(i);
			tdmat.set_diagonal_entry(i, diag + alpha);
		}
		h = tdmat.solve_linear_system(rhs);
		for (int i = 0; i <= n; i++)
		{
			x[m][i] = x[m - 1][i] + h[i];
		}
		//find infinity norm
		val1 = 0.0,	val2 = 0.0;
		for (int i = 0; i <= n; i++)
		{
			diff1 = fabs(x[m][i] - x[m - 1][i]);
			if (diff1 > val1)
			{
				val1 = diff1;
			}
			//finding norm of the difference between
			//x and the global solution
			diff2 = fabs(x[m][i] - 1.0);
			if (diff2 < val2)
			{
				val2 = diff2;
			}
		}
		if (val1 < TOL)
		{
			if (val2 < TOL)
			{
				cout << "Converged to true global minimizer." << endl;
			}
			else
			{
				cout << "Converged to local minimizer." << endl;
			}
			converged = true;
			cout << "Convergence reached in " << m << " iterations" << endl;
		}
		else
		{
			m++;
			x.resize(m + 1);
			x[m].resize(n + 1);
		}
	}
	if (!converged && m >= maxIters)
	{
		cout << "Convergence not reached." << endl;
	}
	system("pause");
	return x;
}

vector<double> IterMethod::GaussSeidel(double(*f)(double x), double(*r)(double x))
{
	/*This method performs Gauss-Seidel iterative method to solve
	  a linear system that governs the solution to a two-point
	  boundary value problem. */
	int maxIters = 10000;
	double TOL = pow(10, -16);
	bool converged = false;
	
	//Our previous iteration level vector
	vector<double> w(n + 1);
	//Our current iteration level vector
	vector<double> v(n + 1);
	vector<double> x = getPartition();

	//diagonal values of the matrix
	double alpha1 = -1.0*getAlpha()[0][1];
	double alpha2 = -1.0*getAlpha()[1][2];
	double alpha3 = -1.0*getAlpha()[2][4];

	//create initial guess
	for (int i = 0; i <= n; i++)
	{
		w[i] = 0.0;
	}

	int m = 1;
	v[0] = 0.0;
	v[n] = 0.0;
	double val = 0.0;
	double diff;
	while (!converged && m < maxIters)
	{
		v[1] = (f(x[1]) + MatrixVectorMultGS(1, v, w)) / (alpha1 + r(x[1]));
		for (int i = 2; i < n - 1; i++)
		{
			v[i] = (f(x[i]) + MatrixVectorMultGS(i, v, w)) / (alpha2 + r(x[i]));
		}
		v[n - 1] = (f(x[n - 1]) + MatrixVectorMultGS(n - 1, v, w)) / (alpha3 + r(x[n - 1]));
		//find infinity norm
		val = 0.0;
		for (int i = 0; i <= n; i++)
		{
			diff = fabs(v[i] - w[i]);
			if (diff > val)
			{
				val = diff;
			}
		}
		if (val < TOL)
		{
			converged = true;
			cout << "Convergence reached in " << m << " iterations." << endl;
		}
		else
		{
			w = v;
			m++;
		}
	}
	if (!converged && m >= maxIters)
	{
		cout << "Convergence not reached." << endl;
	}
	return v;
}

vector<vector<double>> IterMethod::getAlpha()
{
	//Returns the alpha values that make up the matrix A
	vector<vector<double>> alpha(3, vector<double>(6));
	alpha[0][0] = 5.0 / (6.0*h*h), alpha[0][1] = -5.0 / (4.0*h*h); 
	alpha[0][2] = -1.0 / (3.0*h*h), alpha[0][3] = 7.0 / (6.0*h*h);
	alpha[0][4] = -1.0 / (2.0*h*h), alpha[0][5] = 1.0 / (12.0*h*h);
	alpha[1][0] = -1.0 / (12.0*h*h), alpha[1][1] = 4.0 / (3.0*h*h);
	alpha[1][2] = -5.0 / (2.0*h*h), alpha[1][3] = 4.0 / (3.0*h*h);
	alpha[1][4] = -1.0 / (12.0*h*h);
	alpha[2][0] = 1.0 / (12.0*h*h), alpha[2][1] = -1.0 / (2.0*h*h);
	alpha[2][2] = 7.0 / (6.0*h*h), alpha[2][3] = -1.0 / (3.0*h*h);
	alpha[2][4] = -5.0 / (4.0*h*h), alpha[2][5] = 5.0 / (6.0*h*h);

	return alpha;
}

double IterMethod::MatrixVectorMultGS(int i, vector<double> v, vector<double> w)
{
	/*Performs the partial matrix-vector multiplication for a given i (between 1 and n).
	  Needed for Gauss-Seidel iterative method. 

	  Vector v is the iteration level vector.
	  Vector w is the previous iteration level vector. */

	vector<vector<double>> alpha = getAlpha();
	if (i == 1)
	{
		return alpha[0][2] * w[i + 1] + alpha[0][3] * w[i + 2] 
			+ alpha[0][4] * w[i + 3] + alpha[0][5] * w[i + 4];
	}
	else if (i == 2)
	{
		return alpha[1][1] * v[i - 1] + alpha[1][3] * w[i + 1]
			+ alpha[1][4] * w[i + 2];
	}
	else if (i == n - 1)
	{
		return alpha[2][0] * v[i - 4] + alpha[2][1] * v[i - 3] + alpha[2][2] * v[i - 2]
			+ alpha[2][3] * v[i - 1] + alpha[2][5] * w[i + 1];
	}
	else if (i == n)
	{
		return 0.0;
	}
	else
	{
		return alpha[1][0] * v[i - 2] + alpha[1][1] * v[i - 1] 
			+ alpha[1][3] * w[i + 1] + alpha[1][4] * w[i + 2];
	}
}

vector<double> IterMethod::LinearIterMethod(double alpha, double(*f)(double x), double(*r)(double x))
/*This method performs a linear iterative method to solve
  a linear system that governs the solution to a two-point
  boundary value problem. */
{
	int maxIters = 10000;
	double TOL = pow(10, -16);
	bool converged = false;
	
	vector<double> U(n + 1);
	vector<double> Um(n + 1);

	for (int i = 0; i <= n; i++)
	{
		U[i] = 0.0;
	}
	vector<double> rhs;
	tridiagonal_matrix tdmat = getMatrix(alpha, r);

	int m = 1;
	double val, diff;
	while (!converged && m < maxIters)
	{
		rhs = getRHS(alpha, U, f);
		Um = tdmat.solve_linear_system(rhs);
		val = 0.0;
		for (int i = 0; i <= n; i++)
		{
			diff = fabs(Um[i] - U[i]);
			if (diff > val)
			{
				val = diff;
			}
		}
		if (val < TOL)
		{
			converged = true;
			cout << "Convergence reached in " << m << " iterations." << endl;
		}
		else
		{
			U = Um;
			m++;
		}
	}
	if (!converged && m >= maxIters)
	{
		cout << "Convergence not reached." << endl;
	}
	
	return Um;
}

vector<double> IterMethod::getRHS(double alpha, vector<double> U, double(*f)(double x))
{
	vector<double> rhs(n + 1);
	vector<double> x = getPartition();
	vector<vector<double>> A = getAlpha();

	rhs[0] = alpha * U[0];
	rhs[1] = alpha * U[1] - A[0][3] * U[3] - A[0][4] * U[4] - A[0][5] * U[5]; 
	rhs[1] += f(x[1]);
	for (int i = 2; i < n - 1; i++)
	{
		rhs[i] = alpha * U[i] - A[1][0] * U[i - 2] - A[1][4] * U[i + 2];
		rhs[i] += f(x[i]);
	}
	rhs[n - 1] = alpha * U[n - 1] - A[2][0] * U[n - 4] - A[2][1] * U[n - 3] - A[2][2] * U[n - 2];
	rhs[n - 1] += f(x[n - 1]);
	rhs[n] = alpha * U[n];

	return rhs;
}

tridiagonal_matrix IterMethod::getMatrix(double alpha, double(*r)(double x))
{
	tridiagonal_matrix tdmat = new tridiagonal_matrix(n + 1);
	vector<double> x = getPartition();
	vector<vector<double>> A = getAlpha();

	tdmat.set_diagonal_entry(0, 1.0 + alpha);
	tdmat.set_diagonal_entry(1, A[0][1] + r(x[1]) + alpha);
	tdmat.set_lower_diagonal_entry(0, A[0][0]);
	tdmat.set_upper_diagonal_entry(0, 0.0);
	tdmat.set_upper_diagonal_entry(1, A[0][2]);
	for (int i = 2; i < n - 1; i++)
	{
		tdmat.set_diagonal_entry(i, A[1][2] + r(x[i]) + alpha);
	}
	for (int i = 1; i < n - 2; i++)
	{
		tdmat.set_lower_diagonal_entry(i, A[1][1]);
		tdmat.set_upper_diagonal_entry(i + 1, A[1][3]);
	}
	tdmat.set_diagonal_entry(n - 1, A[2][4] + r(x[n - 1]) + alpha);
	tdmat.set_diagonal_entry(n, 1.0 + alpha);
	tdmat.set_upper_diagonal_entry(n - 1, A[2][5]);
	tdmat.set_lower_diagonal_entry(n - 2, A[2][3]);
	tdmat.set_lower_diagonal_entry(n - 1, 0.0);

	return tdmat;
}