package com.core.utils;

import com.epam.reportportal.cucumber.AbstractReporter;
import io.reactivex.Maybe;

import javax.annotation.Nonnull;
import java.util.Optional;

public class StepReporter extends AbstractReporter {
    private static final String RP_STORY_TYPE = "STORY";
    private static final String RP_TEST_TYPE = "SCENARIO";

    public StepReporter() {
        super();
    }

    @Override
    @Nonnull
    protected Optional<Maybe<String>> getRootItemId() {
        return Optional.empty();
    }

    @Override
    @Nonnull
    protected String getFeatureTestItemType() {
        return RP_STORY_TYPE;
    }

    @Override
    @Nonnull
    protected String getScenarioTestItemType() {
        return RP_TEST_TYPE;
    }
}
