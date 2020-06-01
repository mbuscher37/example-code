import java.util.ArrayList;
import javax.swing.JOptionPane;

public class MatchMaker
{
    private static final String TITLE           = "Match Maker Database";
    private static final String GET_ID          = "Enter the client's id: ";
    private static final String GET_AGE         = "Enter the client's age: ";
    private static final String GET_GENDER      = "Enter the client's gender:\n(M for male, F for female)";
    private static final String MORE_HOBBIES    = "\nEnter more hobbies? \n(Enter the hobby or\nN or No to finish)";
    private static final String MORE_MEMBERS    = "Enter more members? \n(Y or Yes to enter more,\nN or No to exit and print report)";
    private static final String SHOW_MEMBERS    = "Show the member list?\n(Y or Yes to print the member list)";
    private static final String HOBBY_ADDED     = "The new hobby has been added.";
    private static final String MEMBER_ADDED    = "The new member has been added.";
    private static final int QSTN               = JOptionPane.QUESTION_MESSAGE;
    private static final int INFO               = JOptionPane.INFORMATION_MESSAGE;

    private static final String[] ID            = {"seriously38", "babyblueeyes", "holyroller", "M3hImL33t", "naturegrl"};
    private static final int[] AGE              = { 38, 22, 32, 18, 27 };
    private static final String[] GENDER           = {"F", "F", "M", "M", "F"};
    private static final String[] HOBBIES       = {"Bowling", "Fishing", "Snow Boarding", "Skiing", "Soccer", "Hockey", "TV", "Reading",
            "Hunting", "Soccer", "Volleyball", "Tennis", "Cooking", "Eating", "Movies"};

    public static void main(String[] args)
    {
        String input;                           //holds Strings returned from showInputDialog()
        String id;                              //client's id
        String gender;                          //client's gender
        int age;                                //client's age
        Member tempMember;                      //temporary member;
        int randomIndex;                        //used for the random index into an array
        boolean done = false;                   //used to determine if the user is done making entries
        boolean correct = false;                //used to make sure an input is in the correct form
        boolean found = false;                  //used to determine if a new entry is already in the members ArrayList
        ArrayList<Member> members;              //ArrayList of members
        members = new ArrayList<Member>();

        for (int index = 0; (index < ID.length) && (index < AGE.length)
        && (index < GENDER.length); index++)
        //can use any one of the arrays since they should all be the 
        //same length
        {
            tempMember = new Member(ID[index],AGE[index],GENDER[index]);
            randomIndex = (int)(Math.random()*HOBBIES.length);
            tempMember.addHobby(HOBBIES[randomIndex]);
            randomIndex = (int)(Math.random()*HOBBIES.length);
            tempMember.addHobby(HOBBIES[randomIndex]);
            members.add(tempMember);
        }

        while (!done)
        {
            input = JOptionPane.showInputDialog(null,SHOW_MEMBERS,
                TITLE,QSTN);
            testForCancel(input);
            if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes"))
            {
                showMembers(members);
            }
            id = JOptionPane.showInputDialog(null,GET_ID,TITLE,QSTN);
            testForCancel(id);
            input = JOptionPane.showInputDialog(null,GET_AGE,TITLE,QSTN);
            testForCancel(input);
            age = Integer.parseInt(input);
            while (!correct)
            {
                input = JOptionPane.showInputDialog(null,GET_GENDER,
                    TITLE,QSTN);
                testForCancel(input);
                if (input.toUpperCase().equals("M") || input.toUpperCase().equals("F"))
                {
                    correct = true;
                }
            }
            gender = input;
            correct = false;
            tempMember = new Member(id,age,gender);
            for (Member member : members)
            {
                if (member.memberEquals(id))
                {
                    found = true;
                    addHobbiesToMember(member);
                }
            }
            if (!found)
            {
                addHobbiesToMember(tempMember);
                members.add(tempMember);
                JOptionPane.showMessageDialog(null,MEMBER_ADDED,
                    TITLE,INFO);
            }

            input = JOptionPane.showInputDialog(null,MORE_MEMBERS,
                TITLE,QSTN);
            testForCancel(input);
            if (input.toLowerCase().equals("y") || input.toLowerCase().equals("yes"))
            {
                found = false;
            }
            else
            {
                done = true;
                showMembers(members);
            }
        }
        System.exit(0);
    }

    public static void addHobbiesToMember(Member member) 
    //has to be static because main is static (is being called in main)
    {
        String input; //not needed
        String output;
        String hobby;
        boolean finished = false;
        while (!finished)
        {
            output = member.getHobbies() + MORE_HOBBIES; 
            hobby = JOptionPane.showInputDialog(null,output,
                TITLE,QSTN);
            testForCancel(hobby);
            if ((hobby.toLowerCase()).equals("n") 
            || (hobby.toLowerCase()).equals("no")) //or hobby.equalsIgnoreCase("no"))
            {
                finished = true;
            }
            else
            {
                member.addHobby(hobby);
                JOptionPane.showMessageDialog(null,HOBBY_ADDED,
                    TITLE,INFO);
            }
        }
    }

    private static void testForCancel(String input)
    //can be public or private
    {
        if (input == null)
        {
            System.exit(0);
        }
    }

    public static void showMembers(ArrayList<Member> members)
    //can be public or private
    {
        System.out.println(TITLE); 
        //or System.out.println("Match Maker Database");
        for (Member member : members)
        {
            System.out.println("----------------------");
            System.out.println(member.memberToString());
        }
    }
}
