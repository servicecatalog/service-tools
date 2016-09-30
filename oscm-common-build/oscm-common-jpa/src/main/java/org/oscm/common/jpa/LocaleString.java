/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 7, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.jpa;

import java.util.Map;
import java.util.TreeMap;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.SequenceGenerator;

/**
 * Data class for localized strings
 * 
 * @author miethaner
 */
@Entity(name = LocaleString.ENTITY_NAME)
public class LocaleString {

    public static final String ENTITY_NAME = "Locale_String";
    public static final String COLLECTION_NAME = "Translation_String";
    public static final String FIELD_ID = "id";
    public static final String FIELD_LANG_KEY = "lang_key";
    public static final String FIELD_TRANSLATION = "translation";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ls_seq")
    @SequenceGenerator(name = "ls_seq", allocationSize = 1000)
    @Column(name = FIELD_ID)
    private Long id;

    @ElementCollection
    @MapKeyColumn(name = FIELD_LANG_KEY)
    @Column(name = FIELD_TRANSLATION)
    @CollectionTable(name = COLLECTION_NAME)
    private Map<String, String> translations;

    public LocaleString() {
        translations = new TreeMap<>();
    }

    public LocaleString(Map<String, String> translations) {
        setTranslations(translations);
    }

    public Map<String, String> getTranslations() {
        return new TreeMap<>(translations);
    }

    public void setTranslations(Map<String, String> translations) {
        if (translations != null) {
            this.translations = translations;
        } else {
            this.translations = new TreeMap<>();
        }
    }
}
