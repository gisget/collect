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
public class OfcDataErrorReport extends org.jooq.impl.TableImpl<org.openforis.collect.persistence.jooq.tables.records.OfcDataErrorReportRecord> {

	private static final long serialVersionUID = 182635749;

	/**
	 * The singleton instance of <code>collect.ofc_data_error_report</code>
	 */
	public static final org.openforis.collect.persistence.jooq.tables.OfcDataErrorReport OFC_DATA_ERROR_REPORT = new org.openforis.collect.persistence.jooq.tables.OfcDataErrorReport();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.openforis.collect.persistence.jooq.tables.records.OfcDataErrorReportRecord> getRecordType() {
		return org.openforis.collect.persistence.jooq.tables.records.OfcDataErrorReportRecord.class;
	}

	/**
	 * The column <code>collect.ofc_data_error_report.id</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcDataErrorReportRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>collect.ofc_data_error_report.query_id</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcDataErrorReportRecord, java.lang.Integer> QUERY_ID = createField("query_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>collect.ofc_data_error_report.creation_date</code>.
	 */
	public final org.jooq.TableField<org.openforis.collect.persistence.jooq.tables.records.OfcDataErrorReportRecord, java.sql.Timestamp> CREATION_DATE = createField("creation_date", org.jooq.impl.SQLDataType.TIMESTAMP, this, "");

	/**
	 * Create a <code>collect.ofc_data_error_report</code> table reference
	 */
	public OfcDataErrorReport() {
		this("ofc_data_error_report", null);
	}

	/**
	 * Create an aliased <code>collect.ofc_data_error_report</code> table reference
	 */
	public OfcDataErrorReport(java.lang.String alias) {
		this(alias, org.openforis.collect.persistence.jooq.tables.OfcDataErrorReport.OFC_DATA_ERROR_REPORT);
	}

	private OfcDataErrorReport(java.lang.String alias, org.jooq.Table<org.openforis.collect.persistence.jooq.tables.records.OfcDataErrorReportRecord> aliased) {
		this(alias, aliased, null);
	}

	private OfcDataErrorReport(java.lang.String alias, org.jooq.Table<org.openforis.collect.persistence.jooq.tables.records.OfcDataErrorReportRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.openforis.collect.persistence.jooq.Collect.COLLECT, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<org.openforis.collect.persistence.jooq.tables.records.OfcDataErrorReportRecord> getPrimaryKey() {
		return org.openforis.collect.persistence.jooq.Keys.OFC_DATA_ERROR_REPORT_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<org.openforis.collect.persistence.jooq.tables.records.OfcDataErrorReportRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.openforis.collect.persistence.jooq.tables.records.OfcDataErrorReportRecord>>asList(org.openforis.collect.persistence.jooq.Keys.OFC_DATA_ERROR_REPORT_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<org.openforis.collect.persistence.jooq.tables.records.OfcDataErrorReportRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<org.openforis.collect.persistence.jooq.tables.records.OfcDataErrorReportRecord, ?>>asList(org.openforis.collect.persistence.jooq.Keys.OFC_DATA_ERROR_REPORT__OFC_DATA_ERROR_REPORT_QUERY_FKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.openforis.collect.persistence.jooq.tables.OfcDataErrorReport as(java.lang.String alias) {
		return new org.openforis.collect.persistence.jooq.tables.OfcDataErrorReport(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.openforis.collect.persistence.jooq.tables.OfcDataErrorReport rename(java.lang.String name) {
		return new org.openforis.collect.persistence.jooq.tables.OfcDataErrorReport(name, null);
	}
}
