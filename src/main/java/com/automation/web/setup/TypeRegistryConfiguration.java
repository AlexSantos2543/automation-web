package com.automation.web.setup;

import com.automation.web.pageobjects.PageObject;
import com.automation.web.pages.LoginSsoPage;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.cucumberexpressions.Transformer;
import io.cucumber.datatable.DataTableType;

import java.util.Locale;

public class TypeRegistryConfiguration implements TypeRegistryConfigurer {

    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {

        typeRegistry.defineParameterType(new ParameterType<>(
                "page",
                ".+",
                Class.class,
                new Transformer<Class>() {
                    @Override
                    public Class<? extends PageObject> transform(String arg) {
                        arg = arg.toLowerCase();

                        switch (arg) {
                            case "login":
                            case "Login page":
                                return LoginSsoPage.class;
                            default:
                                throw new RuntimeException("Could not parse page: " + arg);
                        }
                    }
                }
        ));
//
//        typeRegistry.defineParameterType(new ParameterType<>(
//                "locationCategory",
//                ".*",
//                LocationCategory.class,
//                (String arg) -> LocationCategory.valueOf(arg.toUpperCase())));
//
//        typeRegistry.defineParameterType(new ParameterType<>(
//                "upcomingPast",
//                "upcoming|past",
//                TripType.class,
//                (String arg) -> TripType.valueOf(arg.toUpperCase())
//        ));
//
//        typeRegistry.defineDataTableType(new DataTableType(HotelSegmentModal.Model.class,
//                (TableEntryTransformer<HotelSegmentModal.Model>) entry ->
//                        new HotelSegmentModal.Model(
//                                entry.get("city"),
//                                entry.get("hotel"),
//                                Integer.parseInt(entry.get("check-in")),
//                                Integer.parseInt(entry.get("check-out")),
//                                entry.get("confirmation number"),
//                                entry.get("notes"),
//                                entry.get("guest"),
//                                entry.get("nights"),
//                                entry.get("affinity"),
//                                entry.get("cancelation policy"),
//                                entry.get("address"),
//                                entry.get("contact number"))));
//
//        typeRegistry.defineDataTableType(new DataTableType(FlightConnectionModal.Model.class,
//                (TableEntryTransformer<FlightConnectionModal.Model>) entry ->
//                        new FlightConnectionModal.Model(
//                                entry.get("departure airport"),
//                                entry.get("arrival airport"),
//                                Integer.parseInt(entry.get("departure future days")),
//                                Integer.parseInt(entry.get("arrival future days")),
//                                Integer.parseInt(entry.get("departure future hours")),
//                                Integer.parseInt(entry.get("arrival future hours")),
//                                entry.get("airline"),
//                                entry.get("meal"),
//                                entry.get("flight number"),
//                                entry.get("terminal"),
//                                entry.get("aircraft"),
//                                entry.get("gate"),
//                                entry.get("name"),
//                                entry.get("freq flyer"),
//                                entry.get("seat"),
//                                entry.get("class"),
//                                entry.get("confirmation number"),
//                                entry.get("notes"))));
//
//        typeRegistry.defineDataTableType(new DataTableType(RailSegmentModal.Model.class,
//                (TableEntryTransformer<RailSegmentModal.Model>) entry ->
//                        new RailSegmentModal.Model(
//                                entry.get("departure city"),
//                                entry.get("departure station"),
//                                Integer.parseInt(entry.get("departure future days")),
//                                entry.get("arrival city"),
//                                entry.get("arrival station"),
//                                Integer.parseInt(entry.get("arrival future days")),
//                                entry.get("coach"),
//                                entry.get("seat"),
//                                entry.get("train class"),
//                                entry.get("operated by"),
//                                entry.get("train number"),
//                                entry.get("confirmation number"),
//                                entry.get("notes"))));
    }
}
