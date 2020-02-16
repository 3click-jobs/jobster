import React from 'react';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';


const useStyles = makeStyles(theme => ({
  // formControl: {
  //   margin: theme.spacing(1),
  //   minWidth: 80,
  // },
  // selectEmpty: {
  //   backgroundColor: 'white',
  //   padding: '2px',
  // },
  langButton: {
    color: 'white',
    '& img': {
      // top: '1px',
      height: '25px'
    }
  },
  langMenu: {
    '& a': {
      textDecoration: 'none',
    },
    '& img': {
      // top: '1px',
      height: '25px'
    }
  },
}));

const LanguageSelector = () => {

    const classes = useStyles();
    const { i18n } = useTranslation()

    const [anchorEl, setAnchorEl] = React.useState(null);

    const isLangMenuOpen = Boolean(anchorEl)

    const handleClick = event => {
      setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
      setAnchorEl(null);
    };

    const handleLang = lang => {
      i18n.changeLanguage(lang);
      handleClose();
    }

    // const changeLanguage = name => (event) => {
    //   i18n.changeLanguage(event.target.value)
    // }

    const langMenuId = 'language-menu'

    const renderMenu = ( 
      <Menu
        className={classes.langMenu} 
        id={langMenuId}
        anchorEl={anchorEl}
        keepMounted
        open={isLangMenuOpen}
        onClose={handleClose}
      >
        <MenuItem onClick={() => handleLang('en')}><img alt="3 Click Jobs" src='/img/en.png' /></MenuItem>
        <MenuItem onClick={() => handleLang('sr')}><img alt="3 Click Jobs" src='/img/sr.png' /></MenuItem>
      </Menu>
    )

    return (
      <React.Fragment>
        <Button 
          className={classes.langButton} 
          aria-label="language select"
          aria-controls={langMenuId}
          aria-haspopup="true" 
          onClick={handleClick}
        >
          <img alt="3 Click Jobs" src={`/img/${i18n.language}.png`} />
          {/* {i18n.language} */}
        </Button>
        {renderMenu}
      </React.Fragment>
      // <FormControl className={classes.formControl}>
      //   <Select
      //     native
      //     className={classes.selectEmpty}
      //     value={i18n.language}
      //     onChange={changeLanguage('language')}
      //     inputProps={{
      //       name: 'language',
      //       id: 'language-native-simple',
      //     }}
      //   >
      //     <option value={'en'}>EN</option>
      //     <option value={'sr'}>SR</option>
      //   </Select>
      // </FormControl>
    )
  }
  
  export default LanguageSelector