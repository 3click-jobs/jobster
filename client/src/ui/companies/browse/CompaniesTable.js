import React from 'react'

import PropTypes from 'prop-types'

/**
 * Table to display company data. Obsolete.
 */
export const CompaniesTable = ({
  CompaniesTable
}) => {
  console.log('Companies mounted ... bad!')

  return (
    <div>
      
    </div>
  )
}

CompaniesTable.propTypes = {
  /**
   * Table structure (columns and rows) with company data.
   */
  CompaniesTable: PropTypes.object
}

export default CompaniesTable