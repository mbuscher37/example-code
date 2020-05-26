// Programming Assigment #3
// COSC 2030
// Written by Meghan Buscher
// Last updated 11/21/2019

#include <iostream>
#include <string>
#include <cmath>

#define HASHSIZE 333103 //Prime number!

using namespace std;

template <class dtype>
class myHash
{
private:
	dtype hash[HASHSIZE];
	int size;
	int compares;
public:
	myHash();  // constructor
	int findHash(dtype item);
	void insert(dtype item);
	bool find(dtype item);
	int getSize();
	int getCompares();
	void resetCompares();
	~myHash(); //destructor
};


template <class dtype>
myHash<dtype>::myHash()
/* Constructor for the Hash class. */
{
	hash[HASHSIZE];
	for (int i = 0; i < HASHSIZE; i++)
	{
		hash[i] = "";
	}
	size = 0;
	compares = 0;
}

template <class dtype>
int myHash<dtype>::findHash(dtype item)
/* This is the hash function. It generates a key given an item. */
{
	long int val = 0;
	for (int i = 0; i < item.size(); i++)
	{
		//val += (i + 1) * pow((int)item[i], i + 1);
		val += (int)item[i] * (i + 1) * (i + 1) * (i + 1) * (i + 1); //Hash function that Professor Ward wrote
	}
	//divide by the size of the array to make sure you always get something within bounds
	return val % HASHSIZE;
}

template <class dtype>
int myHash<dtype>::getSize()
/* Returns the number of items stored in the hash.
Note: This is not necessarily the same as hashsize. */
{
	return size;
}

template <class dtype>
void myHash<dtype>::insert(dtype item)
/* Inserts an item into a Hash using linear probing. */
{
	int key = findHash(item);
	if (hash[key] == "")
	{
		hash[key] = item;
		size++;
	}
	else
	{
		bool entered = false;
		while (!entered) {
			key++; //linear probing
			if (key >= HASHSIZE)
			{
				key = 0;
			}
			if (hash[key] == "") {
				hash[key] = item;
				entered = true;
				size++;
			}
		}
	}
}

template <class dtype>
bool myHash<dtype>::find(dtype item)
/* Finds an item in a Hash using linear probing. */
{
	compares++;
	int key = findHash(item);
	if (hash[key].compare(item) == 0)
	{     //found it
		compares++;
		return true;
	}
	else if (hash[key].compare("") == 0)
	{
		compares += 2;
		return false;
	}
	else
	{
		compares += 2;
		bool found = false;
		while (!found)
		{
			key++; //linear probing
			if (key >= HASHSIZE)
			{
				key = 0;
			}
			if (hash[key].compare(item) == 0)
			{
				compares++;
				found = true;
			}
			else if (hash[key].compare("") == 0)
			{
				compares += 2;
				return false;
			}
		}
		return found;

	}
}

template <class dtype>
int myHash<dtype>::getCompares()
/* Returns the number of compares after the find function has been called. */
{
	return compares;
}

template <class dtype>
void myHash<dtype>::resetCompares()
/* Resets the number of compares. */
{
	compares = 0;
}

template <class dtype>
myHash<dtype>::~myHash()
/* Destructor for Hash class. */
{
	//;
}
