/**
 * This class is generated by jOOQ
 */
package org.openforis.collect.persistence.jooq.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.2" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OfcTaxon extends org.jooq.impl.TableImpl<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord> {

	private static final long serialVersionUID = -1261796528;

	/**
	 * The singleton instance of <code>collect.ofc_taxon</code>
	 */
	public static final org.openforis.collect.persistence.jooq.tables.OfcTaxon OFC_TAXON = new org.openforis.collect.persistence.jooq.tables.OfcTaxon();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord> getRecordType() {
		return org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord.class;
	}

	/**
	 * The column <code>collect.ofc_taxon.id</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>collect.ofc_taxon.taxon_id</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord, java.lang.Integer> TAXON_ID = createField("taxon_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>collect.ofc_taxon.code</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord, java.lang.String> CODE = createField("code", org.jooq.impl.SQLDataType.VARCHAR.length(32), this, "");

	/**
	 * The column <code>collect.ofc_taxon.scientific_name</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord, java.lang.String> SCIENTIFIC_NAME = createField("scientific_name", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

	/**
	 * The column <code>collect.ofc_taxon.taxon_rank</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord, java.lang.String> TAXON_RANK = createField("taxon_rank", org.jooq.impl.SQLDataType.VARCHAR.length(128).nullable(false), this, "");

	/**
	 * The column <code>collect.ofc_taxon.taxonomy_id</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord, java.lang.Integer> TAXONOMY_ID = createField("taxonomy_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>collect.ofc_taxon.step</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord, java.lang.Integer> STEP = createField("step", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>collect.ofc_taxon.parent_id</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord, java.lang.Integer> PARENT_ID = createField("parent_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * Create a <code>collect.ofc_taxon</code> table reference
	 */
	public OfcTaxon() {
		this("ofc_taxon", null);
	}

	/**
	 * Create an aliased <code>collect.ofc_taxon</code> table reference
	 */
	public OfcTaxon(java.lang.String alias) {
		this(alias, org.openforis.collect.persistence.jooq.tables.OfcTaxon.OFC_TAXON);
	}

	private OfcTaxon(java.lang.String alias, org.jooq.Table<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord> aliased) {
		this(alias, aliased, null);
	}

	private OfcTaxon(java.lang.String alias, org.jooq.Table<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.openforis.collect.persistence.jooq.Collect.COLLECT, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord> getPrimaryKey() {
		return org.openforis.collect.persistence.jooq.Keys.OFC_TAXON_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord>>asList(org.openforis.collect.persistence.jooq.Keys.OFC_TAXON_PKEY, org.openforis.collect.persistence.jooq.Keys.OFC_TAXON_ID_KEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<org.openforis.collect.persistence.jooq.tables.records.OfcTaxonRecord, ?>>asList(org.openforis.collect.persistence.jooq.Keys.OFC_TAXON__OFC_TAXON_TAXONOMY_FKEY, org.openforis.collect.persistence.jooq.Keys.OFC_TAXON__OFC_TAXON_PARENT_FKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.openforis.collect.persistence.jooq.tables.OfcTaxon as(java.lang.String alias) {
		return new org.openforis.collect.persistence.jooq.tables.OfcTaxon(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.openforis.collect.persistence.jooq.tables.OfcTaxon rename(java.lang.String name) {
		return new org.openforis.collect.persistence.jooq.tables.OfcTaxon(name, null);
	}
}
