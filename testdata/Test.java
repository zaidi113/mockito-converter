import org.jmock.Mock;

/**
 * Created by muhammadraza on 07/02/2016.
 */
public class TestWrittenInJMock {

    private Mock someObject = mock(Something.class, "Blahhh");
    private Mock barObject = mock(Bar.class, "Blahhh");
    private Mock person = mock(Person.class, "Blahhh");
    private Bar noMock;

    public void testBlahh(){

        int x = 45;
        barObject.stubs().method("barMethod").with(eq("myString"), ANYTHING, isA("TEST")).will(returnValue(new TranslationPair<String, Boolean>("AA", true)));
        mockRepository.method("TestWrittenInJMock").with(eq(my.proxy()), ANYTHING, isA("TEST")).will(returnValue(new Pair<String, Boolean>("AA", true)));
    }

    public void testFoo(){

        mockRepository.stubs().method("findStringTranslationForBbgDayCountAndDesc").with(eq(ANYTHING), ANYTHING, isA("TEST")).will(returnValue(new TranslationPair<String, Boolean>("AA", true)));
        mockRepository.method("TestWrittenInJMock").with(eq(my.proxy()), ANYTHING, isA("TEST")).will(returnValue(new Pair<String, Boolean>("AA", true)));
    }
}

---------


        import org.jmock.Mock;

/**
 * Created by muhammadraza on 07/02/2016.
 */
public class TestWrittenInJMock {

    private Mock someObject = mock(Something.class, "Blahhh");
    private Mock barObject = mock(Bar.class, "Blahhh");
    private Mock person = mock(Person.class, "Blahhh");
    private Bar noMock;

    public void testBlahh(){

        String fname = "First Name";
        String lname = "Last Name";
        int x = 45;
        barObject.stubs().method("barMethod").with(eq("TEST")).will(returnValue("AA"));
        barObject.stubs().method("barMethod").will(returnValue("AA"));
        barObject.stubs().method("someMethod").with(eq(fname), same(lname)).will(returnValue(new Person<String, Integer>(fname, lname)));

    }

}
-------- Mixed Statements - Order should be kept

import org.jmock.Mock;

/**
 * Created by muhammadraza on 07/02/2016.
 */
public class TestWrittenInJMock {

    private Mock someObject = mock(Something.class, "Blahhh");
    private Mock barObject = mock(Bar.class, "Blahhh");
    private Mock person = mock(Person.class, "Blahhh");
    private Bar noMock;

    public void testBlahh(){

        String fname = "First Name";

        barObject.stubs().method("barMethod").with(eq("TEST"));
        int x = 45;
        barObject.stubs().method("barMethod").will(returnValue("AA"));
        String lname = "Last Name";

        barObject.stubs().method("someMethod").with(eq(fname), same(lname)).will(returnValue(new Person<String, Integer>(fname, lname)));

    }

}

--------

◊
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
        String lname = "Last Name";
        int x = 45;
        barObject.stubs().method("barMethod").with(eq("TEST")).will(returnValue("AA"));
        barObject.stubs().method("barMethod").will(returnValue("AA"));
        barObject.stubs().method("someMethod").with(eq(fname), same(lname)).will(returnValue(new Person<String, Integer>(fname, lname)));

    }

}
