package com.mysema.query.jdoql;

import org.junit.Test;


/**
 * Tests for JDOQL queries of collections and maps.
 */
public class JDOQLContainerTest {

    @Test
    public void testContainsFieldOfCandidate() {

//        "SELECT FROM " + Farm.class.getName()
//                + " WHERE animals.contains(pet)");
    }

    /**
     * Tests NOT contains in Map.values
     */
    @Test
    public void testNotContainsValuesInMapFields() {

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsValue(wrd) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsValue(wrd) && !this.wardrobes.containsValue(wrd2) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2");
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsValue(wrd) && !this.wardrobes.containsValue(wrd2) && this.wardrobes.containsValue(wrd1) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2,org.jpox.samples.models.fitness.Wardrobe wrd1");

    }

    /**
     * Tests NOT contains in Map.values
     */
    @Test public void testNotContainsValuesInMapFieldsInverse() {

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobesInverse.containsValue(wrd) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobesInverse.containsValue(wrd) && !this.wardrobesInverse.containsValue(wrd2) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2");
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobesInverse.containsValue(wrd) && !this.wardrobesInverse.containsValue(wrd2) && this.wardrobesInverse.containsValue(wrd1) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2,org.jpox.samples.models.fitness.Wardrobe wrd1");

    }

    /**
     * Tests NOT contains in Map.keys
     */
    @Test public void testNotContainsKeysInMapFields() {

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes2.containsKey(wrd) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes2.containsKey(wrd) && !this.wardrobes2.containsKey(wrd2) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2");
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes2.containsKey(wrd) && !this.wardrobes2.containsKey(wrd2) && this.wardrobes2.containsKey(wrd1) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2,org.jpox.samples.models.fitness.Wardrobe wrd1");

    }

    /**
     * Tests NOT contains in Map.keys
     */
    @Test public void testNotContainsKeysInMapFieldsInverse() {

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobesInverse2.containsKey(wrd) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobesInverse2.containsKey(wrd) && !this.wardrobesInverse2.containsKey(wrd2) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2");
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobesInverse2.containsKey(wrd) && !this.wardrobesInverse2.containsKey(wrd2) && this.wardrobesInverse2.containsKey(wrd1) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2,org.jpox.samples.models.fitness.Wardrobe wrd1");

    }

    /**
     * Tests NOT contains in Map.entry
     */
    @Test public void testNotContainsEntryInMapFields() {

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsEntry(wrd.model,wrd) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsEntry(wrd.model,wrd) && !this.wardrobes.containsEntry(wrd2.model,wrd2) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2");
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsEntry(wrd.model,wrd) && !this.wardrobes.containsEntry(wrd2.model,wrd2) && this.wardrobes.containsEntry(wrd1.model,wrd1) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2,org.jpox.samples.models.fitness.Wardrobe wrd1");

    }

    /**
     * Tests NOT contains in Map.entry
     */
    @Test public void testNotContainsEntryInMapFieldsInverse() {

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobesInverse.containsEntry(wrd.model,wrd) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobesInverse.containsEntry(wrd.model,wrd) && !this.wardrobesInverse.containsEntry(wrd2.model,wrd2) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2");
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobesInverse.containsEntry(wrd.model,wrd) && !this.wardrobesInverse.containsEntry(wrd2.model,wrd2) && this.wardrobesInverse.containsEntry(wrd1.model,wrd1) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2,org.jpox.samples.models.fitness.Wardrobe wrd1");

    }

    /**
     * Tests get
     */
    @Test public void testGetInMapFields() {

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE this.wardrobes.get(wrd.model) == wrd "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");

    }

    /**
     * Tests get method used in ordering
     */
    @Test public void testGetInOrderingInMapFields() {

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");
//        .setOrdering("this.wardrobes.get(wrd.model).model ascending");

    }


    /**
     * Test for the Map.containsValue() method.
     */
    @Test public void testueryUsesContainsValueRangeVariableAlreadyInuery() {

        
//        "SELECT FROM org.jpox.samples.models.nightlabs_payments.ServerPaymentProcessor\n"
//                + "WHERE\n"
//                + "  modeOfPaymentFlavour.organisationID == paramOrganisationID &&\n"
//                + "  modeOfPaymentFlavour.modeOfPaymentFlavourID == paramModeOfPaymentFlavourID &&\n"
//                + "  modeOfPayment == modeOfPaymentFlavour.modeOfPayment &&\n"
//                + "  this.modeOfPayments.containsValue(modeOfPayment)\n"
//                + "VARIABLES ModeOfPaymentFlavour modeOfPaymentFlavour; ModeOfPayment modeOfPayment\n"
//                + "PARAMETERS String paramOrganisationID, String paramModeOfPaymentFlavourID\n"
//                + "import java.lang.String;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPaymentFlavour;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPayment");
//
//
//        "SELECT FROM org.jpox.samples.models.nightlabs_payments.ServerPaymentProcessor\n"
//                + "WHERE\n"
//                + "  modeOfPaymentFlavour.organisationID == paramOrganisationID &&\n"
//                + "  modeOfPaymentFlavour.modeOfPaymentFlavourID == paramModeOfPaymentFlavourID &&\n"
//                + "  this.modeOfPayments.containsValue(modeOfPaymentFlavour.modeOfPayment)\n"
//                + "VARIABLES ModeOfPaymentFlavour modeOfPaymentFlavour\n"
//                + "PARAMETERS String paramOrganisationID, String paramModeOfPaymentFlavourID\n"
//                + "import java.lang.String;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPaymentFlavour");
//
//
//        "SELECT FROM org.jpox.samples.models.nightlabs_payments.ServerPaymentProcessor\n"
//                + "WHERE\n"
//                + "  modeOfPaymentFlavour.organisationID == paramOrganisationID &&\n"
//                + "  modeOfPaymentFlavour.modeOfPaymentFlavourID == paramModeOfPaymentFlavourID &&\n"
//                + "  modeOfPayment == modeOfPaymentFlavour.modeOfPayment &&\n"
//                + "  this.modeOfPayments.containsValue(modeOfPaymentFlavour.modeOfPayment)\n"
//                + "VARIABLES ModeOfPaymentFlavour modeOfPaymentFlavour; ModeOfPayment modeOfPayment\n"
//                + "PARAMETERS String paramOrganisationID, String paramModeOfPaymentFlavourID\n"
//                + "import java.lang.String;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPaymentFlavour");

    }

    /**
     * Test for the Map.containsKey() method.
     */
    @Test public void testueryUsesContainsKeyRangeVariableAlreadyInuery() {

//        "SELECT FROM org.jpox.samples.models.nightlabs_payments.ServerPaymentProcessor\n"
//                + "WHERE\n"
//                + "  modeOfPaymentFlavour.organisationID == paramOrganisationID &&\n"
//                + "  modeOfPaymentFlavour.modeOfPaymentFlavourID == paramModeOfPaymentFlavourID &&\n"
//                + "  modeOfPayment == modeOfPaymentFlavour.modeOfPayment &&\n"
//                + "  this.modeOfPaymentsKey.containsKey(modeOfPayment)\n"
//                + "VARIABLES ModeOfPaymentFlavour modeOfPaymentFlavour; ModeOfPayment modeOfPayment\n"
//                + "PARAMETERS String paramOrganisationID, String paramModeOfPaymentFlavourID\n"
//                + "import java.lang.String;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPaymentFlavour;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPayment");
//
//
//        "SELECT FROM org.jpox.samples.models.nightlabs_payments.ServerPaymentProcessor\n"
//                + "WHERE\n"
//                + "  modeOfPaymentFlavour.organisationID == paramOrganisationID &&\n"
//                + "  modeOfPaymentFlavour.modeOfPaymentFlavourID == paramModeOfPaymentFlavourID &&\n"
//                + "  this.modeOfPaymentsKey.containsKey(modeOfPaymentFlavour.modeOfPayment)\n"
//                + "VARIABLES ModeOfPaymentFlavour modeOfPaymentFlavour\n"
//                + "PARAMETERS String paramOrganisationID, String paramModeOfPaymentFlavourID\n"
//                + "import java.lang.String;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPaymentFlavour");
//
//
//        "SELECT FROM org.jpox.samples.models.nightlabs_payments.ServerPaymentProcessor\n"
//                + "WHERE\n"
//                + "  modeOfPaymentFlavour.organisationID == paramOrganisationID &&\n"
//                + "  modeOfPaymentFlavour.modeOfPaymentFlavourID == paramModeOfPaymentFlavourID &&\n"
//                + "  modeOfPayment == modeOfPaymentFlavour.modeOfPayment &&\n"
//                + "  this.modeOfPaymentsKey.containsKey(modeOfPaymentFlavour.modeOfPayment)\n"
//                + "VARIABLES ModeOfPaymentFlavour modeOfPaymentFlavour; ModeOfPayment modeOfPayment\n"
//                + "PARAMETERS String paramOrganisationID, String paramModeOfPaymentFlavourID\n"
//                + "import java.lang.String;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPaymentFlavour");

    }

    /**
     * Test for the Map.containsEntry() method.
     */
    @Test public void testueryUsesContainsEntryRangeVariableAlreadyInuery() {

//        "SELECT FROM org.jpox.samples.models.nightlabs_payments.ServerPaymentProcessor\n"
//                + "WHERE\n"
//                + "  modeOfPaymentFlavour.organisationID == paramOrganisationID &&\n"
//                + "  modeOfPaymentFlavour.modeOfPaymentFlavourID == paramModeOfPaymentFlavourID &&\n"
//                + "  modeOfPayment == modeOfPaymentFlavour.modeOfPayment &&\n"
//                + "  this.modeOfPaymentsKey.containsEntry(modeOfPayment,modeOfPayment.primaryKey)\n"
//                + "VARIABLES ModeOfPaymentFlavour modeOfPaymentFlavour; ModeOfPayment modeOfPayment\n"
//                + "PARAMETERS String paramOrganisationID, String paramModeOfPaymentFlavourID\n"
//                + "import java.lang.String;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPaymentFlavour;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPayment");
//
//
//        "SELECT FROM org.jpox.samples.models.nightlabs_payments.ServerPaymentProcessor\n"
//                + "WHERE\n"
//                + "  modeOfPaymentFlavour.organisationID == paramOrganisationID &&\n"
//                + "  modeOfPaymentFlavour.modeOfPaymentFlavourID == paramModeOfPaymentFlavourID &&\n"
//                + "  this.modeOfPaymentsKey.containsEntry(modeOfPaymentFlavour.modeOfPayment,modeOfPaymentFlavour.modeOfPayment.primaryKey)\n"
//                + "VARIABLES ModeOfPaymentFlavour modeOfPaymentFlavour\n"
//                + "PARAMETERS String paramOrganisationID, String paramModeOfPaymentFlavourID\n"
//                + "import java.lang.String;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPaymentFlavour");
//
//
//        "SELECT FROM org.jpox.samples.models.nightlabs_payments.ServerPaymentProcessor\n"
//                + "WHERE\n"
//                + "  modeOfPaymentFlavour.organisationID == paramOrganisationID &&\n"
//                + "  modeOfPaymentFlavour.modeOfPaymentFlavourID == paramModeOfPaymentFlavourID &&\n"
//                + "  modeOfPayment == modeOfPaymentFlavour.modeOfPayment &&\n"
//                + "  this.modeOfPaymentsKey.containsEntry(modeOfPaymentFlavour.modeOfPayment,modeOfPaymentFlavour.modeOfPayment.primaryKey)\n"
//                + "VARIABLES ModeOfPaymentFlavour modeOfPaymentFlavour; ModeOfPayment modeOfPayment\n"
//                + "PARAMETERS String paramOrganisationID, String paramModeOfPaymentFlavourID\n"
//                + "import java.lang.String;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPaymentFlavour");

    }

    /**
     * Test for the Set.contains() method.
     */
    @Test public void testueryUsesContainsSetRangeVariableAlreadyInuery() {

//        "SELECT FROM org.jpox.samples.models.nightlabs_payments.ServerPaymentProcessor\n"
//                + "WHERE\n"
//                + "  modeOfPaymentFlavour.organisationID == paramOrganisationID &&\n"
//                + "  modeOfPaymentFlavour.modeOfPaymentFlavourID == paramModeOfPaymentFlavourID &&\n"
//                + "  modeOfPayment == modeOfPaymentFlavour.modeOfPayment &&\n"
//                + "  this.modeOfPaymentsSet.contains(modeOfPayment)\n"
//                + "VARIABLES ModeOfPaymentFlavour modeOfPaymentFlavour; ModeOfPayment modeOfPayment\n"
//                + "PARAMETERS String paramOrganisationID, String paramModeOfPaymentFlavourID\n"
//                + "import java.lang.String;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPaymentFlavour;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPayment");
//
//
//        "SELECT FROM org.jpox.samples.models.nightlabs_payments.ServerPaymentProcessor\n"
//                + "WHERE\n"
//                + "  modeOfPaymentFlavour.organisationID == paramOrganisationID &&\n"
//                + "  modeOfPaymentFlavour.modeOfPaymentFlavourID == paramModeOfPaymentFlavourID &&\n"
//                + "  this.modeOfPaymentsSet.contains(modeOfPaymentFlavour.modeOfPayment)\n"
//                + "VARIABLES ModeOfPaymentFlavour modeOfPaymentFlavour\n"
//                + "PARAMETERS String paramOrganisationID, String paramModeOfPaymentFlavourID\n"
//                + "import java.lang.String;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPaymentFlavour");
//
//
//        "SELECT FROM org.jpox.samples.models.nightlabs_payments.ServerPaymentProcessor\n"
//                + "WHERE\n"
//                + "  modeOfPaymentFlavour.organisationID == paramOrganisationID &&\n"
//                + "  modeOfPaymentFlavour.modeOfPaymentFlavourID == paramModeOfPaymentFlavourID &&\n"
//                + "  modeOfPayment == modeOfPaymentFlavour.modeOfPayment &&\n"
//                + "  this.modeOfPaymentsSet.contains(modeOfPaymentFlavour.modeOfPayment)\n"
//                + "VARIABLES ModeOfPaymentFlavour modeOfPaymentFlavour; ModeOfPayment modeOfPayment\n"
//                + "PARAMETERS String paramOrganisationID, String paramModeOfPaymentFlavourID\n"
//                + "import java.lang.String;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPaymentFlavour");

    }

    /**
     * Test for the List.contains() method.
     */
    @Test public void testueryUsesContainsListRangeVariableAlreadyInuery() {

//        "SELECT FROM org.jpox.samples.models.nightlabs_payments.ServerPaymentProcessor\n"
//                + "WHERE\n"
//                + "  modeOfPaymentFlavour.organisationID == paramOrganisationID &&\n"
//                + "  modeOfPaymentFlavour.modeOfPaymentFlavourID == paramModeOfPaymentFlavourID &&\n"
//                + "  modeOfPayment == modeOfPaymentFlavour.modeOfPayment &&\n"
//                + "  this.modeOfPaymentsList.contains(modeOfPayment)\n"
//                + "VARIABLES ModeOfPaymentFlavour modeOfPaymentFlavour; ModeOfPayment modeOfPayment\n"
//                + "PARAMETERS String paramOrganisationID, String paramModeOfPaymentFlavourID\n"
//                + "import java.lang.String;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPaymentFlavour;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPayment");
//
//
//        "SELECT FROM org.jpox.samples.models.nightlabs_payments.ServerPaymentProcessor\n"
//                + "WHERE\n"
//                + "  modeOfPaymentFlavour.organisationID == paramOrganisationID &&\n"
//                + "  modeOfPaymentFlavour.modeOfPaymentFlavourID == paramModeOfPaymentFlavourID &&\n"
//                + "  this.modeOfPaymentsList.contains(modeOfPaymentFlavour.modeOfPayment)\n"
//                + "VARIABLES ModeOfPaymentFlavour modeOfPaymentFlavour\n"
//                + "PARAMETERS String paramOrganisationID, String paramModeOfPaymentFlavourID\n"
//                + "import java.lang.String;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPaymentFlavour");
//
//
//        "SELECT FROM org.jpox.samples.models.nightlabs_payments.ServerPaymentProcessor\n"
//                + "WHERE\n"
//                + "  modeOfPaymentFlavour.organisationID == paramOrganisationID &&\n"
//                + "  modeOfPaymentFlavour.modeOfPaymentFlavourID == paramModeOfPaymentFlavourID &&\n"
//                + "  modeOfPayment == modeOfPaymentFlavour.modeOfPayment &&\n"
//                + "  this.modeOfPaymentsList.contains(modeOfPaymentFlavour.modeOfPayment)\n"
//                + "VARIABLES ModeOfPaymentFlavour modeOfPaymentFlavour; ModeOfPayment modeOfPayment\n"
//                + "PARAMETERS String paramOrganisationID, String paramModeOfPaymentFlavourID\n"
//                + "import java.lang.String;\n"
//                + "import org.jpox.samples.models.nightlabs_payments.ModeOfPaymentFlavour");

    }
}