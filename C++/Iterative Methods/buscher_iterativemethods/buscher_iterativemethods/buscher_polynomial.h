#include <ccomplex>
#ifndef POLYNOMIAL_H
#define POLYNOMIAL_H

/* 
	This class represents a polynomial of a certain degree with real valued
	coefficients. Evaluation of the polynomial is assumed to be at a complex 
	number.
	*/

using namespace std;

class polynomial
{
private:
	int degree;
	vector<double> coefficients;

public:
	void setDegree(int n);
	void setCoefficients(vector<double> &coeffs);
	void setCoefficient(int i, double coeff_i);
	int getDegree();
	vector<double> getCoefficients();
	double getCoefficient(int i);

	//Evaluate the polynomial at z using Horner's Algoritm.
	complex<double> calcValAt(complex<double> &z);

	//Evaluate the polynomial and its derivative at z using Horner's Algorithm.
	vector<complex<double>> calcValandDerAt(complex<double> &z);

};

#endif // !POLYNOMIAL_H

