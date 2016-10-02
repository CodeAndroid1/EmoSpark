package ibm.mobile.ibmmobileappbuilder.action;

import android.content.Context;
import android.content.Intent;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import ibmmobileappbuilder.actions.ActivityIntentLauncher;
import util.ClassImpostorizerJunitRuleMockery;

import static org.hamcrest.CoreMatchers.sameInstance;

public class ActivityIntentLauncherTest {

    private static final Intent AN_INTENT = new Intent();

    @Rule public JUnitRuleMockery mockery = new ClassImpostorizerJunitRuleMockery();
    @Mock private Context mockContext;

    private final ActivityIntentLauncher intentLauncher = new ActivityIntentLauncher();

    @Test
    public void startsActivity() {
        mockery.checking(
                new Expectations() {{
                    oneOf(mockContext).startActivity(with(sameInstance(AN_INTENT)));
                }}
        );

        intentLauncher.start(mockContext, AN_INTENT);
    }
}