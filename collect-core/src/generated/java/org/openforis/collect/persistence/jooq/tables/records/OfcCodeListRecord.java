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
public class OfcCodeListRecord extends org.jooq.impl.UpdatableRecordImpl<org.openforis.collect.persistence.jooq.tables.records.OfcCodeListRecord> implements org.jooq.Record18<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Boolean, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer> {

	private static final long serialVersionUID = 491562598;

	/**
	 * Setter for <code>collect.ofc_code_list.id</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.id</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>collect.ofc_code_list.survey_id</code>.
	 */
	public void setSurveyId(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.survey_id</code>.
	 */
	public java.lang.Integer getSurveyId() {
		return (java.lang.Integer) getValue(1);
	}

	/**
	 * Setter for <code>collect.ofc_code_list.survey_work_id</code>.
	 */
	public void setSurveyWorkId(java.lang.Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.survey_work_id</code>.
	 */
	public java.lang.Integer getSurveyWorkId() {
		return (java.lang.Integer) getValue(2);
	}

	/**
	 * Setter for <code>collect.ofc_code_list.code_list_id</code>.
	 */
	public void setCodeListId(java.lang.Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.code_list_id</code>.
	 */
	public java.lang.Integer getCodeListId() {
		return (java.lang.Integer) getValue(3);
	}

	/**
	 * Setter for <code>collect.ofc_code_list.item_id</code>.
	 */
	public void setItemId(java.lang.Integer value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.item_id</code>.
	 */
	public java.lang.Integer getItemId() {
		return (java.lang.Integer) getValue(4);
	}

	/**
	 * Setter for <code>collect.ofc_code_list.parent_id</code>.
	 */
	public void setParentId(java.lang.Integer value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.parent_id</code>.
	 */
	public java.lang.Integer getParentId() {
		return (java.lang.Integer) getValue(5);
	}

	/**
	 * Setter for <code>collect.ofc_code_list.sort_order</code>.
	 */
	public void setSortOrder(java.lang.Integer value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.sort_order</code>.
	 */
	public java.lang.Integer getSortOrder() {
		return (java.lang.Integer) getValue(6);
	}

	/**
	 * Setter for <code>collect.ofc_code_list.code</code>.
	 */
	public void setCode(java.lang.String value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.code</code>.
	 */
	public java.lang.String getCode() {
		return (java.lang.String) getValue(7);
	}

	/**
	 * Setter for <code>collect.ofc_code_list.qualifiable</code>.
	 */
	public void setQualifiable(java.lang.Boolean value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.qualifiable</code>.
	 */
	public java.lang.Boolean getQualifiable() {
		return (java.lang.Boolean) getValue(8);
	}

	/**
	 * Setter for <code>collect.ofc_code_list.since_version_id</code>.
	 */
	public void setSinceVersionId(java.lang.Integer value) {
		setValue(9, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.since_version_id</code>.
	 */
	public java.lang.Integer getSinceVersionId() {
		return (java.lang.Integer) getValue(9);
	}

	/**
	 * Setter for <code>collect.ofc_code_list.deprecated_version_id</code>.
	 */
	public void setDeprecatedVersionId(java.lang.Integer value) {
		setValue(10, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.deprecated_version_id</code>.
	 */
	public java.lang.Integer getDeprecatedVersionId() {
		return (java.lang.Integer) getValue(10);
	}

	/**
	 * Setter for <code>collect.ofc_code_list.label1</code>.
	 */
	public void setLabel1(java.lang.String value) {
		setValue(11, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.label1</code>.
	 */
	public java.lang.String getLabel1() {
		return (java.lang.String) getValue(11);
	}

	/**
	 * Setter for <code>collect.ofc_code_list.label2</code>.
	 */
	public void setLabel2(java.lang.String value) {
		setValue(12, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.label2</code>.
	 */
	public java.lang.String getLabel2() {
		return (java.lang.String) getValue(12);
	}

	/**
	 * Setter for <code>collect.ofc_code_list.label3</code>.
	 */
	public void setLabel3(java.lang.String value) {
		setValue(13, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.label3</code>.
	 */
	public java.lang.String getLabel3() {
		return (java.lang.String) getValue(13);
	}

	/**
	 * Setter for <code>collect.ofc_code_list.description1</code>.
	 */
	public void setDescription1(java.lang.String value) {
		setValue(14, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.description1</code>.
	 */
	public java.lang.String getDescription1() {
		return (java.lang.String) getValue(14);
	}

	/**
	 * Setter for <code>collect.ofc_code_list.description2</code>.
	 */
	public void setDescription2(java.lang.String value) {
		setValue(15, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.description2</code>.
	 */
	public java.lang.String getDescription2() {
		return (java.lang.String) getValue(15);
	}

	/**
	 * Setter for <code>collect.ofc_code_list.description3</code>.
	 */
	public void setDescription3(java.lang.String value) {
		setValue(16, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.description3</code>.
	 */
	public java.lang.String getDescription3() {
		return (java.lang.String) getValue(16);
	}

	/**
	 * Setter for <code>collect.ofc_code_list.level</code>.
	 */
	public void setLevel(java.lang.Integer value) {
		setValue(17, value);
	}

	/**
	 * Getter for <code>collect.ofc_code_list.level</code>.
	 */
	public java.lang.Integer getLevel() {
		return (java.lang.Integer) getValue(17);
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
	// Record18 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row18<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Boolean, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer> fieldsRow() {
		return (org.jooq.Row18) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row18<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Boolean, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer> valuesRow() {
		return (org.jooq.Row18) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.SURVEY_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field3() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.SURVEY_WORK_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field4() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.CODE_LIST_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field5() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.ITEM_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field6() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.PARENT_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field7() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.SORT_ORDER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field8() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.CODE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Boolean> field9() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.QUALIFIABLE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field10() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.SINCE_VERSION_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field11() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.DEPRECATED_VERSION_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field12() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.LABEL1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field13() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.LABEL2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field14() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.LABEL3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field15() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.DESCRIPTION1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field16() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.DESCRIPTION2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field17() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.DESCRIPTION3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field18() {
		return org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST.LEVEL;
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
		return getSurveyId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value3() {
		return getSurveyWorkId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value4() {
		return getCodeListId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value5() {
		return getItemId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value6() {
		return getParentId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value7() {
		return getSortOrder();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value8() {
		return getCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Boolean value9() {
		return getQualifiable();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value10() {
		return getSinceVersionId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value11() {
		return getDeprecatedVersionId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value12() {
		return getLabel1();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value13() {
		return getLabel2();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value14() {
		return getLabel3();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value15() {
		return getDescription1();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value16() {
		return getDescription2();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value17() {
		return getDescription3();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value18() {
		return getLevel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value2(java.lang.Integer value) {
		setSurveyId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value3(java.lang.Integer value) {
		setSurveyWorkId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value4(java.lang.Integer value) {
		setCodeListId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value5(java.lang.Integer value) {
		setItemId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value6(java.lang.Integer value) {
		setParentId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value7(java.lang.Integer value) {
		setSortOrder(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value8(java.lang.String value) {
		setCode(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value9(java.lang.Boolean value) {
		setQualifiable(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value10(java.lang.Integer value) {
		setSinceVersionId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value11(java.lang.Integer value) {
		setDeprecatedVersionId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value12(java.lang.String value) {
		setLabel1(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value13(java.lang.String value) {
		setLabel2(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value14(java.lang.String value) {
		setLabel3(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value15(java.lang.String value) {
		setDescription1(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value16(java.lang.String value) {
		setDescription2(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value17(java.lang.String value) {
		setDescription3(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord value18(java.lang.Integer value) {
		setLevel(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcCodeListRecord values(java.lang.Integer value1, java.lang.Integer value2, java.lang.Integer value3, java.lang.Integer value4, java.lang.Integer value5, java.lang.Integer value6, java.lang.Integer value7, java.lang.String value8, java.lang.Boolean value9, java.lang.Integer value10, java.lang.Integer value11, java.lang.String value12, java.lang.String value13, java.lang.String value14, java.lang.String value15, java.lang.String value16, java.lang.String value17, java.lang.Integer value18) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached OfcCodeListRecord
	 */
	public OfcCodeListRecord() {
		super(org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST);
	}

	/**
	 * Create a detached, initialised OfcCodeListRecord
	 */
	public OfcCodeListRecord(java.lang.Integer id, java.lang.Integer surveyId, java.lang.Integer surveyWorkId, java.lang.Integer codeListId, java.lang.Integer itemId, java.lang.Integer parentId, java.lang.Integer sortOrder, java.lang.String code, java.lang.Boolean qualifiable, java.lang.Integer sinceVersionId, java.lang.Integer deprecatedVersionId, java.lang.String label1, java.lang.String label2, java.lang.String label3, java.lang.String description1, java.lang.String description2, java.lang.String description3, java.lang.Integer level) {
		super(org.openforis.collect.persistence.jooq.tables.OfcCodeList.OFC_CODE_LIST);

		setValue(0, id);
		setValue(1, surveyId);
		setValue(2, surveyWorkId);
		setValue(3, codeListId);
		setValue(4, itemId);
		setValue(5, parentId);
		setValue(6, sortOrder);
		setValue(7, code);
		setValue(8, qualifiable);
		setValue(9, sinceVersionId);
		setValue(10, deprecatedVersionId);
		setValue(11, label1);
		setValue(12, label2);
		setValue(13, label3);
		setValue(14, description1);
		setValue(15, description2);
		setValue(16, description3);
		setValue(17, level);
	}
}
