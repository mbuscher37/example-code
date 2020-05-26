#include <iostream>
#include <complex>
#include <vector>
#include "buscher_polynomial.h"

using namespace std;

void polynomial::setDegree(int n)
{
	degree = n;
	coefficients.resize(degree + 1);
}

void polynomial::setCoefficients(vector<double> &coeffs)
{
	for (int i = 0; i <= degree; i++)
	{
		coefficients[i] = coeffs[i];
	}
}

void polynomial::setCoefficient(int i, double coeff_i)
{
	coefficients[i] = coeff_i;
}

int polynomial::getDegree()
{
	return degree;
}

double polynomial::getCoefficient(int i)
{
	return coefficients[i];
}

vector<double> polynomial::getCoefficients()
{
	return coefficients;
}

complex<double> polynomial::calcValAt(complex<double> &z)
{
	complex<double> val;
	val = coefficients[degree];
	for (int i = degree - 1; i >= 0; i--)
	{
		val = coefficients[i] + z * val;
	}
	return val;
}

vector<complex<double>> polynomial::calcValandDerAt(complex<double> &z)
{
	vector<complex<double>> val(2);
	val[0] = coefficients[degree];
	val[1] = complex<double>(0.0, 0.0);
	for (int i = degree - 1; i >= 0; i--)
	{
		val[1] = val[0] + z * val[1];
		val[0] = coefficients[i] + z * val[0];
	}
	return val;
}
