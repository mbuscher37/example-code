// Programming Assigment #3
// COSC 2030
// Written by Meghan Buscher
// Last updated 11/21/2019

/*This code checks the spelling of words in a book given a dictionary to reference.
It then returns the number of correctly and incorrectly spelled words along with the number of compares. */
#include <iostream>
#include <istream>
#include <ostream>
#include <fstream>
#include <string>
#include "buscher_Hash.h"
#include "timer.h"


using namespace std;

string CleanWord(string word)
// Rids the word of any punctuation, other than apostrophes, and cleans the word. 
{
	for (int i = 0; i < word.length(); i++)
	{
		if ((isalnum(word[i]) != 0) || (isalpha(word[i]) != 0)) // checks if it's a letter or number
		{
			word[i] = ::tolower(word[i]);
		}
		else if (word[i] == '\'')  // its an apostrophe
		{
			word[i] = word[i];
		}
		else
		{
			word.erase(i, 1); // remove the character
			i--;
		}
	}

	return word;
}

myHash<string> readDictionary()
{
	//Reads a dictionary from a text file and loads it into a hash. 
	myHash<string> myDictionary = myHash<string>(); // creates a hash of strings to hold all the words from the dictionary

	string line;
	ifstream myFile("dictionary.txt");
	if (myFile.is_open())
	{
		while (myFile >> line)
		{
			myDictionary.insert(CleanWord(line)); //add scrubbed word to dictionary hash
		}
		myFile.close();
	}
	else
	{
		cout << "Unable to open file";
		exit(1);
	}
	return myDictionary;
}

int main()
{
	// This is where the spell checking is implemented
	Timer time;

	myHash<string> myDictionary = readDictionary(); //Loads dictionary into a hash

	time.Start();

	//Output file of the misspelled words:
	ofstream myOutputfile;
	myOutputfile.open("MisspelledWords.txt");

	//Integer values needed for output:
	int compares = 0;
	int words = 0;
	int correctCompares = 0;
	int correctWords = 0;
	int incorrectCompares = 0;
	int incorrectWords = 0;
	int skippedWords = 0;

	bool found = false;

	string word;
	ifstream myFile("book.txt");
	if (myFile.is_open())
	{
		while (myFile >> word)
		{
			word = CleanWord(word);
			if (word == "")
			{
				;
			}
			else if (isalpha(word[0]))
			{
				found = myDictionary.find(word);
				compares += myDictionary.getCompares();
				if (found) //If word was found in dictionary (i.e. spelled correctly)
				{
					correctCompares += myDictionary.getCompares();
					correctWords++;
				}
				else //If word was not found in dictionary
				{
					incorrectCompares += myDictionary.getCompares();
					incorrectWords++;
					myOutputfile << word << endl;
				}
				myDictionary.resetCompares();
				words++;
			}
			else 
			{
				//If a word starts with a non-letter than it is skipped and not checked in the dictionary. 
				skippedWords++;
			}
		}
		myFile.close();
	}
	else
	{
		cout << "Unable to open file";
		exit(1);
	}
	
	myOutputfile.close(); //Close text file of misspelled words

	time.Stop();

	// Output:
	cout << "dictionary size " << myDictionary.getSize() << endl;
	cout << "Done checking and these are the results" << endl;
	cout << "finished in time: " << time.Time() << endl;
	cout << "There are " << correctWords << " words found in the dictionary" << endl;
	cout << "     " << correctCompares << " compares. Average: " << (correctCompares / correctWords) << endl;
	cout << "There are " << incorrectWords << " words NOT found in the dictionary" << endl;
	cout << "     " << incorrectCompares << " compares. Average: " << (incorrectCompares) / incorrectWords << endl;
	cout << "There are " << skippedWords << " words not checked." << endl;

	system("pause");
	
	// delete data from hash.
	myDictionary.~myHash();

	return 0;
}