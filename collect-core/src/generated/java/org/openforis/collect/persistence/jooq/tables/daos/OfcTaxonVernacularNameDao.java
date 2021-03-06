/**
 * This class is generated by jOOQ
 */
package org.openforis.collect.persistence.jooq.tables.daos;


import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.openforis.collect.persistence.jooq.tables.OfcTaxonVernacularName;
import org.openforis.collect.persistence.jooq.tables.records.OfcTaxonVernacularNameRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OfcTaxonVernacularNameDao extends DAOImpl<OfcTaxonVernacularNameRecord, org.openforis.collect.persistence.jooq.tables.pojos.OfcTaxonVernacularName, Integer> {

	/**
	 * Create a new OfcTaxonVernacularNameDao without any configuration
	 */
	public OfcTaxonVernacularNameDao() {
		super(OfcTaxonVernacularName.OFC_TAXON_VERNACULAR_NAME, org.openforis.collect.persistence.jooq.tables.pojos.OfcTaxonVernacularName.class);
	}

	/**
	 * Create a new OfcTaxonVernacularNameDao with an attached configuration
	 */
	public OfcTaxonVernacularNameDao(Configuration configuration) {
		super(OfcTaxonVernacularName.OFC_TAXON_VERNACULAR_NAME, org.openforis.collect.persistence.jooq.tables.pojos.OfcTaxonVernacularName.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Integer getId(org.openforis.collect.persistence.jooq.tables.pojos.OfcTaxonVernacularName object) {
		return object.getId();
	}

	/**
	 * Fetch records that have <code>id IN (values)</code>
	 */
	public List<org.openforis.collect.persistence.jooq.tables.pojos.OfcTaxonVernacularName> fetchById(Integer... values) {
		return fetch(OfcTaxonVernacularName.OFC_TAXON_VERNACULAR_NAME.ID, values);
	}

	/**
	 * Fetch a unique record that has <code>id = value</code>
	 */
	public org.openforis.collect.persistence.jooq.tables.pojos.OfcTaxonVernacularName fetchOneById(Integer value) {
		return fetchOne(OfcTaxonVernacularName.OFC_TAXON_VERNACULAR_NAME.ID, value);
	}

	/**
	 * Fetch records that have <code>vernacular_name IN (values)</code>
	 */
	public List<org.openforis.collect.persistence.jooq.tables.pojos.OfcTaxonVernacularName> fetchByVernacularName(String... values) {
		return fetch(OfcTaxonVernacularName.OFC_TAXON_VERNACULAR_NAME.VERNACULAR_NAME, values);
	}

	/**
	 * Fetch records that have <code>language_code IN (values)</code>
	 */
	public List<org.openforis.collect.persistence.jooq.tables.pojos.OfcTaxonVernacularName> fetchByLanguageCode(String... values) {
		return fetch(OfcTaxonVernacularName.OFC_TAXON_VERNACULAR_NAME.LANGUAGE_CODE, values);
	}

	/**
	 * Fetch records that have <code>language_variety IN (values)</code>
	 */
	public List<org.openforis.collect.persistence.jooq.tables.pojos.OfcTaxonVernacularName> fetchByLanguageVariety(String... values) {
		return fetch(OfcTaxonVernacularName.OFC_TAXON_VERNACULAR_NAME.LANGUAGE_VARIETY, values);
	}

	/**
	 * Fetch records that have <code>taxon_id IN (values)</code>
	 */
	public List<org.openforis.collect.persistence.jooq.tables.pojos.OfcTaxonVernacularName> fetchByTaxonId(Integer... values) {
		return fetch(OfcTaxonVernacularName.OFC_TAXON_VERNACULAR_NAME.TAXON_ID, values);
	}

	/**
	 * Fetch records that have <code>step IN (values)</code>
	 */
	public List<org.openforis.collect.persistence.jooq.tables.pojos.OfcTaxonVernacularName> fetchByStep(Integer... values) {
		return fetch(OfcTaxonVernacularName.OFC_TAXON_VERNACULAR_NAME.STEP, values);
	}

	/**
	 * Fetch records that have <code>qualifier1 IN (values)</code>
	 */
	public List<org.openforis.collect.persistence.jooq.tables.pojos.OfcTaxonVernacularName> fetchByQualifier1(String... values) {
		return fetch(OfcTaxonVernacularName.OFC_TAXON_VERNACULAR_NAME.QUALIFIER1, values);
	}

	/**
	 * Fetch records that have <code>qualifier2 IN (values)</code>
	 */
	public List<org.openforis.collect.persistence.jooq.tables.pojos.OfcTaxonVernacularName> fetchByQualifier2(String... values) {
		return fetch(OfcTaxonVernacularName.OFC_TAXON_VERNACULAR_NAME.QUALIFIER2, values);
	}

	/**
	 * Fetch records that have <code>qualifier3 IN (values)</code>
	 */
	public List<org.openforis.collect.persistence.jooq.tables.pojos.OfcTaxonVernacularName> fetchByQualifier3(String... values) {
		return fetch(OfcTaxonVernacularName.OFC_TAXON_VERNACULAR_NAME.QUALIFIER3, values);
	}
}
