import java.util.ArrayList;

public class Member
{
    private ArrayList<String> hobbies;
    private String id;
    private int age;
    private String gender;

    public Member(String memberID, int age, String gender)
    {
        id = memberID;
        this.age = age; //this operator is for the field
        this.gender = gender;
        hobbies = new ArrayList<String>();
    }

    public void addHobby(String hobby)
    {
        hobbies.add(hobby);
    }

    public boolean memberEquals(String memberID)
    {
        boolean equals = false;
        if (id.toLowerCase().equals(memberID.toLowerCase()))
        {
            equals = true;
        }
        return equals;
    }

    public String memberToString()
    {
        return "ID: " + id + "\nGender: " + gender 
                + "\nAge: " + age + "\nHobbies:" + "\n"
                + getHobbies();
    }

    public String getHobbies()
    {
        String retHobbies = "";
        int length = hobbies.size();
        for (String hobby : hobbies)
        {
            retHobbies += hobby;
            --length;
            if (length > 0)
            {
                retHobbies += ", ";
            }
            else
            {
                retHobbies += ".";
            }
        }
        return retHobbies;
    }
    
    public String getID()
    {
        return id;
    }
}
