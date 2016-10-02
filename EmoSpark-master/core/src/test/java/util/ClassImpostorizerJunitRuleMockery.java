package util;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;

public class ClassImpostorizerJunitRuleMockery extends JUnitRuleMockery {
    public ClassImpostorizerJunitRuleMockery() {
        super();
        setImposteriser(ClassImposteriser.INSTANCE);
    }
}
