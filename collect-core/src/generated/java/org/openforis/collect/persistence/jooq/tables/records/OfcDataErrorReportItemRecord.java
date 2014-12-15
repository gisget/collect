/**
 * This class is generated by jOOQ
 */
package org.openforis.collect.persistence.jooq.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.2" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OfcDataErrorReportItemRecord extends org.jooq.impl.UpdatableRecordImpl<org.openforis.collect.persistence.jooq.tables.records.OfcDataErrorReportItemRecord> implements org.jooq.Record7<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String> {

	private static final long serialVersionUID = 650079802;

	/**
	 * Setter for <code>collect.ofc_data_error_report_item.id</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>collect.ofc_data_error_report_item.id</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>collect.ofc_data_error_report_item.report_id</code>.
	 */
	public void setReportId(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>collect.ofc_data_error_report_item.report_id</code>.
	 */
	public java.lang.Integer getReportId() {
		return (java.lang.Integer) getValue(1);
	}

	/**
	 * Setter for <code>collect.ofc_data_error_report_item.record_id</code>.
	 */
	public void setRecordId(java.lang.Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>collect.ofc_data_error_report_item.record_id</code>.
	 */
	public java.lang.Integer getRecordId() {
		return (java.lang.Integer) getValue(2);
	}

	/**
	 * Setter for <code>collect.ofc_data_error_report_item.parent_entity_id</code>.
	 */
	public void setParentEntityId(java.lang.Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>collect.ofc_data_error_report_item.parent_entity_id</code>.
	 */
	public java.lang.Integer getParentEntityId() {
		return (java.lang.Integer) getValue(3);
	}

	/**
	 * Setter for <code>collect.ofc_data_error_report_item.node_index</code>.
	 */
	public void setNodeIndex(java.lang.Integer value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>collect.ofc_data_error_report_item.node_index</code>.
	 */
	public java.lang.Integer getNodeIndex() {
		return (java.lang.Integer) getValue(4);
	}

	/**
	 * Setter for <code>collect.ofc_data_error_report_item.value</code>.
	 */
	public void setValue(java.lang.String value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>collect.ofc_data_error_report_item.value</code>.
	 */
	public java.lang.String getValue() {
		return (java.lang.String) getValue(5);
	}

	/**
	 * Setter for <code>collect.ofc_data_error_report_item.status</code>.
	 */
	public void setStatus(java.lang.String value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>collect.ofc_data_error_report_item.status</code>.
	 */
	public java.lang.String getStatus() {
		return (java.lang.String) getValue(6);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Integer> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record7 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row7<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String> fieldsRow() {
		return (org.jooq.Row7) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row7<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String> valuesRow() {
		return (org.jooq.Row7) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return org.openforis.collect.persistence.jooq.tables.OfcDataErrorReportItem.OFC_DATA_ERROR_REPORT_ITEM.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return org.openforis.collect.persistence.jooq.tables.OfcDataErrorReportItem.OFC_DATA_ERROR_REPORT_ITEM.REPORT_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field3() {
		return org.openforis.collect.persistence.jooq.tables.OfcDataErrorReportItem.OFC_DATA_ERROR_REPORT_ITEM.RECORD_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field4() {
		return org.openforis.collect.persistence.jooq.tables.OfcDataErrorReportItem.OFC_DATA_ERROR_REPORT_ITEM.PARENT_ENTITY_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field5() {
		return org.openforis.collect.persistence.jooq.tables.OfcDataErrorReportItem.OFC_DATA_ERROR_REPORT_ITEM.NODE_INDEX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field6() {
		return org.openforis.collect.persistence.jooq.tables.OfcDataErrorReportItem.OFC_DATA_ERROR_REPORT_ITEM.VALUE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field7() {
		return org.openforis.collect.persistence.jooq.tables.OfcDataErrorReportItem.OFC_DATA_ERROR_REPORT_ITEM.STATUS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value2() {
		return getReportId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value3() {
		return getRecordId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value4() {
		return getParentEntityId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value5() {
		return getNodeIndex();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value6() {
		return getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value7() {
		return getStatus();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcDataErrorReportItemRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcDataErrorReportItemRecord value2(java.lang.Integer value) {
		setReportId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcDataErrorReportItemRecord value3(java.lang.Integer value) {
		setRecordId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcDataErrorReportItemRecord value4(java.lang.Integer value) {
		setParentEntityId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcDataErrorReportItemRecord value5(java.lang.Integer value) {
		setNodeIndex(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcDataErrorReportItemRecord value6(java.lang.String value) {
		setValue(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcDataErrorReportItemRecord value7(java.lang.String value) {
		setStatus(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcDataErrorReportItemRecord values(java.lang.Integer value1, java.lang.Integer value2, java.lang.Integer value3, java.lang.Integer value4, java.lang.Integer value5, java.lang.String value6, java.lang.String value7) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached OfcDataErrorReportItemRecord
	 */
	public OfcDataErrorReportItemRecord() {
		super(org.openforis.collect.persistence.jooq.tables.OfcDataErrorReportItem.OFC_DATA_ERROR_REPORT_ITEM);
	}

	/**
	 * Create a detached, initialised OfcDataErrorReportItemRecord
	 */
	public OfcDataErrorReportItemRecord(java.lang.Integer id, java.lang.Integer reportId, java.lang.Integer recordId, java.lang.Integer parentEntityId, java.lang.Integer nodeIndex, java.lang.String value, java.lang.String status) {
		super(org.openforis.collect.persistence.jooq.tables.OfcDataErrorReportItem.OFC_DATA_ERROR_REPORT_ITEM);

		setValue(0, id);
		setValue(1, reportId);
		setValue(2, recordId);
		setValue(3, parentEntityId);
		setValue(4, nodeIndex);
		setValue(5, value);
		setValue(6, status);
	}
}
