#include <iostream>
#include <vector>
#include "buscher_interpolatingpolynomial.h"
#include "buscher_tridiagonal_matrix.h"

using namespace std;

InterpolatingPolynomial::InterpolatingPolynomial(vector<vector<double> > &data)
{
	Data = data;
	NumSubIntervals = Data.size() - 1;
	subintervallength.resize(NumSubIntervals);
	for (int i = 0; i < NumSubIntervals; i++)
	{
		subintervallength[i] = Data[i + 1][0] - Data[i][0];
	}
}

int InterpolatingPolynomial::getDegree()
{
	return Degree;
}

int InterpolatingPolynomial::getNumSubIntervals()
{
	return NumSubIntervals;
}

vector<vector<double>> InterpolatingPolynomial::getCurveCoordinates(int nres)
{
	int totpts = NumSubIntervals * nres + 1;
	vector<vector<double>> curve(totpts);
	for (int i = 0; i < totpts; i++)
	{
		curve[i].resize(2);
	}
	double hh;
	int k = 0;
	for (int i = 0; i < NumSubIntervals; i++)
	{
		hh = subintervallength[i] / nres; // increm. length in this interval
		curve[k][0] = Data[i][0];
		curve[k][1] = Evaluate(curve[k][0], i); // Polynom value at curve[k][0]
		k++;
			for (int j = 1; j < nres; j++)
			{
				curve[k][0] = curve[k - 1][0] + hh;
				curve[k][1] = Evaluate(curve[k][0], i);
				k++;
			}
	}
	curve[k][0] = curve[k - 1][0] + hh;
	curve[k][1] = Evaluate(curve[k][0], NumSubIntervals - 1);

	return curve;
}

InterpolatingPolynomial::~InterpolatingPolynomial()
{
	// nothing to implement here
	;
}

Newton::Newton(vector<vector<double>> &data) : InterpolatingPolynomial(data)
{
	Degree = NumSubIntervals;
	Coefficients.resize(Data.size());
	for (int i = 0; i < Data.size(); i++)
	{
		Coefficients[i].resize(1);
	}
	set();
}

double Newton::Evaluate(double x, int subinterval_index = 0)
{
	double val;

	// Implement Horner's algorithm here.
	val = Coefficients[Degree][0];
	for (int i = Degree - 1; i >= 0; i--)
	{
		val = Coefficients[i][0] + val * (x - Data[i][0]);
	}

	return val;
}

Newton::~Newton()
{
	// nothing to implement here
	;
}

void Newton::set()
{
	Coefficients[0][0] = Data[0][1];
	vector<double> divdiff(Degree + 1);
	for (int i = 0; i <= Degree; i++)
	{
		divdiff[i] = Data[i][1];
	}
	Coefficients[0][0] = divdiff[0];
	for (int k = 1; k <= Degree; k++)
	{
		for (int l = Degree - k; l >= 0; l--)
		{
			divdiff[l + k] = (divdiff[l + k] - divdiff[l + k - 1]) / (Data[l + k][0] - Data[l][0]);
		}
		Coefficients[k][0] = divdiff[k];
	}
}

LinearSpline::LinearSpline(vector<vector<double> > &data) : InterpolatingPolynomial(data)
{
	Degree = 1;
	Coefficients.resize(NumSubIntervals);
	for (int i = 0; i < NumSubIntervals; i++)
	{
		Coefficients[i].resize(Degree + 1);
	}
	set();
}

double LinearSpline::Evaluate(double x, int i)
{
	double val;

	// Use the appropriate expression for the calculation.
	double dx1 = Data[i + 1][0] - x;
	double dx2 = x - Data[i][0];
	// fill val here, use dx1 and dx2
	val = Coefficients[i][0] * dx1 + Coefficients[i][1] * dx2;
	return val;
}

LinearSpline::~LinearSpline()
{
	// nothing ot implement here
	;
}

void LinearSpline::set()
{
	/* Every subinterval has two coefficients.
	At subinterval i, Coefficients[i][0] is for the first coefficient and
	Coefficients[i][1] is for the second coefficient. */

	for (int i = 0; i < NumSubIntervals; i++)
	{
		// fill Coefficients[i][j] for j=0,1
		Coefficients[i][0] = Data[i][1] / subintervallength[i];
		Coefficients[i][1] = Data[i + 1][1] / subintervallength[i];
	}
}

CubicSpline::CubicSpline(vector<vector<double> > &data
	, vector<boundary_type> &type
	, vector<double> &bdryval) : InterpolatingPolynomial(data)
{
	Degree = 3;
	BdryType = type;
	BdryVal = bdryval;
	Coefficients.resize(NumSubIntervals);
	for (int i = 0; i < NumSubIntervals; i++)
	{
		Coefficients[i].resize(Degree + 1);
	}
	set();
}

double CubicSpline::Evaluate(double x, int i)
{
	double val;

	// Use the approximate expression for the calculation.
	double dx1 = Data[i + 1][0] - x;
	double dx2 = x - Data[i][0];
	// fill val here, use dx1 and dx2
	val = Coefficients[i][0] * pow(dx1, 3) + Coefficients[i][1] * pow(dx2, 3) 
		+ Coefficients[i][2] * dx1 + Coefficients[i][3] * dx2;
	return val;
}

CubicSpline::~CubicSpline()
{
	// nothing to implement here
	;
}

void CubicSpline::set()
{
	vector<double> z = solve_the_linear_system();

	/* Every subinterval has four coefficients. 
	At subinterval i, there are Coefficients[i][j] for j=0,1,2,3. */
	for (int i = 0; i < NumSubIntervals; i++)
	{
		// fill Coefficients[i][j], j=0,1,2,3.
		Coefficients[i][0] = z[i] / 6.0 / subintervallength[i];
		Coefficients[i][1] = z[i + 1] / 6.0 / subintervallength[i];
		Coefficients[i][2] = Data[i][1] / subintervallength[i] - z[i] * subintervallength[i] / 6.0;
		Coefficients[i][3] = Data[i + 1][1] / subintervallength[i] - z[i + 1] * subintervallength[i] / 6.0;
	}
}

vector<double> CubicSpline::solve_the_linear_system()
{
	vector<double> F(Data.size()); // the right hand side vector.
	tridiagonal_matrix Mat(Data.size());

	vector<double> DD1(NumSubIntervals);
	for (int i = 0; i < NumSubIntervals; i++)
	{
		DD1[i] = (Data[i + 1][1] - Data[i][1]) / subintervallength[i]; // 1st divided diff.
	}

	// left boundary
	if (BdryType[0] == boundary_type::second_derivative)
	{
		Mat.set_diagonal_entry(0, 1.0);
		Mat.set_upper_diagonal_entry(0, 0.0);
		F[0] = BdryVal[0];
	}
	else if (BdryType[0] == boundary_type::first_derivative)
	{
		double dx = Data[1][0] - Data[0][0];
		double diag = -1 * pow(dx, 2) / 2.0 / subintervallength[0] 
			+ subintervallength[0] / 6.0;
		double upper_diag = -1.0*subintervallength[0] / 6.0;
		
		// fill Mat and F
		Mat.set_diagonal_entry(0, diag);
		Mat.set_upper_diagonal_entry(0, upper_diag);
		F[0] = BdryVal[0] - Data[1][1] / subintervallength[0] 
			+ Data[0][1] / subintervallength[0];
	}
	else
	{
		cout << "The boundary type on the left is not recognized. Exiting.\n";
		exit(1);
	}

	// everything internal inbetween the left and right boundary points.
	for (int i = 1; i < NumSubIntervals; i++)
	{
		double diag = subintervallength[i - 1] / 3.0 + subintervallength[i] / 3.0;
		
		// fill Mat and F
		Mat.set_lower_diagonal_entry(i - 1, subintervallength[i - 1] / 6.0);
		Mat.set_diagonal_entry(i, diag);
		Mat.set_upper_diagonal_entry(i, subintervallength[i] / 6.0);
		F[i] = DD1[i] - DD1[i - 1];
	}

	// right boundary
	if (BdryType[1] == boundary_type::second_derivative)
	{
		Mat.set_diagonal_entry(NumSubIntervals, 1.0);
		Mat.set_lower_diagonal_entry(NumSubIntervals - 1, 0.0);
		F[NumSubIntervals] = BdryVal[1];
	}
	else if (BdryType[1] == boundary_type::first_derivative)
	{
		double dx = Data[NumSubIntervals][0] - Data[NumSubIntervals - 1][0];
		double diag = pow(dx, 2) / 2.0 / subintervallength[NumSubIntervals - 1]
			- subintervallength[NumSubIntervals - 1] / 6.0;
		double lower_diag = subintervallength[NumSubIntervals - 1] / 6.0;
		
		// fill Mat and F
		Mat.set_diagonal_entry(NumSubIntervals, diag);
		Mat.set_lower_diagonal_entry(NumSubIntervals - 1, lower_diag);
		F[NumSubIntervals] = BdryVal[1] - Data[NumSubIntervals][1] / subintervallength[NumSubIntervals - 1]
			+ Data[NumSubIntervals - 1][1] / subintervallength[NumSubIntervals - 1];
	}
	else
	{
		cout << "The boundary type on the right is not recognized. Exiting.\n";
		exit(1);
	}
	return Mat.solve_linear_system(F);
}

TensionSpline::TensionSpline(vector<vector<double> > &data, 
	vector<boundary_type> &type, vector<double> &bdryval, 
	vector<double> &tau) : InterpolatingPolynomial(data)
{
	Degree = 4;
	BdryType = type;
	BdryVal = bdryval;
	Tau = tau;
	Coefficients.resize(NumSubIntervals);
	for (int i = 0; i < NumSubIntervals; i++)
	{
		Coefficients[i].resize(Degree + 1);
	}
	set();
}

double TensionSpline::Evaluate(double x, int i)
{
	double val;

	// Use the appropriate expression for the calculation.
	double dx1 = Data[i + 1][0] - x;
	double dx2 = x - Data[i][0];
	// fill val here, use dx1 and dx2
	val = Coefficients[i][0] * sinh(Tau[i] * dx1) + Coefficients[i][1] * sinh(Tau[i] * dx2)
		+ Coefficients[i][2] * dx1 + Coefficients[i][3] * dx2;
	return val;
}

TensionSpline::~TensionSpline()
{
	// nothing to implement here
	;
}

void TensionSpline::set()
{
	vector<double> z = solve_the_linear_system();

	/* Every subinterval has four coefficients.
	At subinterval i, ther are Coeffients[i][j] for j=0,1,2,3. */
	for (int i = 0; i < NumSubIntervals; i++)
	{
		// fill Coefficients[i][j], j=0,1,2,3.
		Coefficients[i][0] = z[i] / pow(Tau[i], 2) 
			/ (sinh(Tau[i] * subintervallength[i]));
		Coefficients[i][1] = z[i + 1] / pow(Tau[i], 2)
			/ (sinh(Tau[i] * subintervallength[i]));
		Coefficients[i][2] = (Data[i][1] - z[i] / pow(Tau[i], 2)) / subintervallength[i];
		Coefficients[i][3] = (Data[i + 1][1] - z[i + 1] / pow(Tau[i], 2)) / subintervallength[i];
	}
}

vector<double> TensionSpline::solve_the_linear_system()
{
	vector<double> F(Data.size()); // the right hand side vector.
	tridiagonal_matrix Mat(Data.size());

	vector<double> alpha(NumSubIntervals);
	vector<double> beta(NumSubIntervals);
	vector<double> gamma(NumSubIntervals);
	for (int i = 0; i < NumSubIntervals; i++)
	{
		// Fill alpha, beta, and gamma here.
		alpha[i] = 1.0 / subintervallength[i] - Tau[i] / sinh(Tau[i] * subintervallength[i]);
		beta[i] = (Tau[i] * cosh(Tau[i] * subintervallength[i])) / sinh(Tau[i] * subintervallength[i]) 
			- 1.0 / subintervallength[i];
		gamma[i] = pow(Tau[i], 2)*(Data[i + 1][1] - Data[i][1]) / subintervallength[i];
	}

	// left boundary
	if (BdryType[0] == boundary_type::second_derivative)
	{
		Mat.set_diagonal_entry(0, 1.0);
		Mat.set_upper_diagonal_entry(0, 0.0);
		F[0] = BdryVal[0];
	}
	else if (BdryType[0] == boundary_type::first_derivative)
	{
		double dx = Data[1][0] - Data[0][0];
		double diag = -1.0 / Tau[0] / sinh(Tau[0] * subintervallength[0]);
		diag = diag + 1.0 / subintervallength[0] / pow(Tau[0], 2);
		double upper_diag = cosh(Tau[0] * dx) / Tau[0] / sinh(Tau[0] * subintervallength[0]);
		upper_diag = upper_diag - 1.0 / subintervallength[0] / pow(Tau[0], 2);

		// fill Mat and F
		Mat.set_diagonal_entry(0, diag);
		Mat.set_upper_diagonal_entry(0, upper_diag);
		F[0] = BdryVal[0] + Data[0][1] / subintervallength[0] - Data[1][1] / subintervallength[0];
	}
	else
	{
		cout << "The boundary type on the left is not recognized. Exiting.\n";
		exit(1);
	}

	// everything internal inbetween the left and right boundary points.
	for (int i = 1; i < NumSubIntervals; i++)
	{
		// fill Mat and F
		Mat.set_lower_diagonal_entry(i, alpha[i - 1]);
		Mat.set_diagonal_entry(i, beta[i - 1] + beta[i]);
		Mat.set_upper_diagonal_entry(i, alpha[i]);
		F[i] = gamma[i] - gamma[i - 1];
	}

	// right boundary
	if (BdryType[1] == boundary_type::second_derivative)
	{
		Mat.set_diagonal_entry(NumSubIntervals, 1.0);
		Mat.set_lower_diagonal_entry(NumSubIntervals-1, 0.0);
		F[NumSubIntervals] = BdryVal[1];
	}
	else if (BdryType[1] == boundary_type::first_derivative)
	{
		double dx = Data[NumSubIntervals][0] - Data[NumSubIntervals - 1][0];
		double diag = cosh(Tau[NumSubIntervals - 1] * dx) 
			/ Tau[NumSubIntervals - 1] / sinh(Tau[NumSubIntervals - 1] 
			* subintervallength[NumSubIntervals - 1]);
		diag = diag - 1.0 / subintervallength[NumSubIntervals - 1] 
			/ pow(Tau[NumSubIntervals - 1], 2);
		double lower_diag = -1.0 / Tau[NumSubIntervals - 1] / sinh(Tau[NumSubIntervals - 1] 
			* subintervallength[NumSubIntervals - 1]);
		lower_diag = lower_diag + 1.0 / subintervallength[NumSubIntervals - 1] 
			/ pow(Tau[NumSubIntervals - 1], 2);

		// fill Mat and F
		Mat.set_diagonal_entry(NumSubIntervals, diag);
		Mat.set_lower_diagonal_entry(NumSubIntervals - 1, lower_diag);
		F[NumSubIntervals] = BdryVal[1] + Data[NumSubIntervals - 1][1] / subintervallength[0] 
			- Data[NumSubIntervals][1] / subintervallength[NumSubIntervals - 1];
	}
	else
	{
		cout << "The boundary type on the right is not recognized. Exiting.\n";
		exit(1);
	}

	return Mat.solve_linear_system(F);
}
