import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

/**
 * Created by muhammadraza on 07/02/2016.
 */
public class TestWrittenInJMock extends MockObjectTestCase{

    private Mock someObject = mock(Something.class, "Blahhh");
    private Mock barObject = mock(Bar.class, "Blahhh");
    private Mock person = mock(Person.class, "Blahhh");
    private Bar noMock;

    public void testBlahh(){

        String fname = "First Name";

        barObject.stubs().method("barMethod").with(eq("TEST")).will(returnValue("AA"));
        int x = 45;
        barObject.stubs().method("barMethod").will(returnValue("AA"));
        String lname = "Last Name";

        barObject.stubs().method("someMethod").with(eq(fname), same(lname)).will(returnValue(new Person<String, Integer>(fname, lname)));

    }

}
