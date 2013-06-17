/**
 * 
 */
package org.openforis.collect.persistence;

import java.util.List;

import org.openforis.idm.metamodel.CodeList;
import org.openforis.idm.metamodel.PersistedCodeListItem;
import org.openforis.idm.metamodel.PersistedCodeListProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author S. Ricci
 *
 */
public class DatabasePersistedCodeListProvider implements
		PersistedCodeListProvider {

	@Autowired
	private CodeListItemDao codeListItemDao;
	
	/* (non-Javadoc)
	 * @see org.openforis.idm.metamodel.PersistedCodeListProvider#getRootItems(org.openforis.idm.metamodel.CodeList)
	 */
	@Override
	public List<PersistedCodeListItem> getRootItems(CodeList codeList) {
		return codeListItemDao.loadRootItems(codeList);
	}

	/* (non-Javadoc)
	 * @see org.openforis.idm.metamodel.PersistedCodeListProvider#getChildItems(org.openforis.idm.metamodel.PersistedCodeListItem)
	 */
	@Override
	public List<PersistedCodeListItem> getChildItems(
			PersistedCodeListItem parentItem) {
		CodeList codeList = parentItem.getCodeList();
		return codeListItemDao.loadItems(codeList, parentItem.getSystemId());
	}

}
