#ifndef INTERPOLATING_POLYNOMIAL_H
#define INTERPOLATING_POLYNOMIAL_H

/* These classes represent polynomials (and splines) that interpolate real 
   valued data (x_i,y_i), i=0,...,n.*/

using namespace std;

// Aparent class
class InterpolatingPolynomial
{
public:

	// A constructior for the parent class. It takes data as input. 
	InterpolatingPolynomial(vector<vector<double> > &data);

	int getDegree();

	int getNumSubIntervals();

	vector<vector<double>> getCoefficients();

	/* A function to calculate a sufficiently fine resolution of polynomial
	values. It returns a matrix having two columns, the first one is absisca,
	while the second one is ordinate (the value of polynomial at absisca).
	It calls a child's polynomial evaluation. */
	vector<vector<double>  > getCurveCoordinates(int num_resolution = 500);

	/* Pure virtual implementation of evaluating a polynomial at a given x. In the
	case of piecewise polynomial, an integer subinterval_index representing
	the subinterval index must be provided. */
	virtual double Evaluate(double x, int subinterval_index = 0) = 0;

	//Destructor.
	virtual ~InterpolatingPolynomial();

protected:

	int Degree;
	int NumSubIntervals;
	vector<vector<double> > Data;
	vector<double> subintervallength;
	vector<vector<double> > Coefficients;
};

//-----------------------------------------------

// A child class named Newton
class Newton : public InterpolatingPolynomial
{
public:

	// A constructor for Newton
	Newton(vector<vector<double> > &data);

	virtual double Evaluate(double x, int subinterval_index);

	~Newton();

private:

	//This is where the coefficients are calculated.
	void set();
};

//-----------------------------------------------

// A child class named LinearSpline
class LinearSpline : public InterpolatingPolynomial
{
public:

	// A constructor for LinearSpline
	LinearSpline(vector<vector<double> > &data);

	virtual double Evaluate(double x, int subinterval_index);

	~LinearSpline();

private:

	// This is where the coefficients are calculated.
	void set();
};

//-----------------------------------------------

/* Boundary type to close the system: either 1st derivative is given, or
2nd derivative is given. */
enum boundary_type { first_derivative = 1, second_derivative = 2 };

// A child class named CubicSpline
class CubicSpline : public InterpolatingPolynomial
{
public:

	/* A constructor for CubicSpline. In addition to data, it takes other inputs
	that are pertinent to this child. */
	CubicSpline(vector<vector<double> > &data, vector<boundary_type> &type,
		vector<double> &bdryval);

	virtual double Evaluate(double x, int subinterval_index);

	~CubicSpline();

private:

	// BdryType[0] is for left, BdryType[1] is for right.
	vector<boundary_type> BdryType;

	// BdryVal[0] is for left, BdryVal[1] is for right.
	vector<double> BdryVal;

	// This is where the coefficients are calculated.
	void set();

	// Solve the linear system governing z.
	vector<double> solve_the_linear_system();
};

//-----------------------------------------------

// A child class named TensionSpline
class TensionSpline : public InterpolatingPolynomial
{
public:

	/* A constructor for TensionSpline. In addition to data, it takes other
	inputs that are pertinent to this child. */
	TensionSpline(vector<vector<double> > &data, vector<boundary_type> &type,
		vector<double> &bdryval, vector<double> &tau);

	virtual double Evaluate(double x, int subinterval_index);

	~TensionSpline();

private:

	// BdryType[0] is for left, BdryType[1] is for right.
	vector<boundary_type> BdryType;

	// BdryVal[0] is for left, BdryVal[1] is for right.
	vector<double> BdryVal;

	// Tension parameter, allow different value for a different subinterval.
	vector<double> Tau;

	// This is where the coefficients are calculated.
	void set();

	// Solve the linear system governing z.
	vector<double> solve_the_linear_system();
};

//-----------------------------------------------

#endif