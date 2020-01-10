// another approach
/*

const companyDto = {
  fields: [
    {
      name: '',
      type: '',
    }
  ],
  defaults: {
    name: value,
    name: value
  }
}


*/


const companyDtoObject = {
  companyName: {
    name: 'companyName',
    type: 'text',
    regexp: '^[_A-Za-z0-9-]{2,}$',
    required: true,
    placeholder: 'Company name',
    default: 'tester'
  },
  // THIS MUST BE IN BECAUSE OF CURRENT PROBLEMS
  companyId: {
    default: '32424'
  },
  companyRegistrationNumber: {
    name: 'companyRegistrationNumber',
    type: 'text',
    regexp: '^[0-9]{14,14}$',
    required: true,
    placeholder: '12345678901234',
    default: '12345678901234'
  },
  mobilePhone: {
    name: 'mobilePhone',
    type: 'text',
    regexp: '^([\\(]{0,1}[\\+]{0,1}[\\(]{0,1}([3][8][1]){0,1}[\\)]{0,1}[- \\.\\/]{0,1}[\\(]{0,1}[0]{0,1}[\\)]{0,1}[6]{1,1}([0-6]|[9]){1,1}[\\)]{0,1}[- \\.\\/]{0,1}(([0-9]{6,7})|([0-9]{2,3}[- \\.\\/]{0,1}[0-9]{2,4}[- \\.\\/]{0,1}[0-9]{0,3})))$',
    required: true,
    placeholder: 'Mobile phone',
    default: '38169010015'
  },
  email: {
    name: 'email',
    type: 'email',
    regexp: '^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$',
    required: true,
    placeholder: 'E-mail',
    default: 'saq0es0s@gmail.com'
  },
  city: {
    name: 'city',
    type: 'text',
    regexp: '^[A-Za-z\\s]+$',
    required: true,
    default: 'Novi Sad'
  },
  country: {
    name: 'country',
    type: 'text',
    regexp: '^[A-Za-z\\s]+$',
    required: true,
    placeholder: 'Country',
    default: 'Serbia'
  },
  iso2Code: {
    name: 'iso2Code',
    type: 'text',
    regexp: '^[A-Za-z]{2,3}$',
    required: true,
    default: 'SER'
  },
  countryRegion: {
    name: 'countryRegion',
    type: 'text',
    regexp: '^[A-Za-z\\s]{0,}$',
    required: true,
    default: 'Vojvodina'
  },
  longitude: {
    name: 'longitude',
    type: 'number',
    min: 180,
    max: 180,
    default: '38.0'
  },
  latitude: {
    name: 'latitude',
    type: 'number',
    min: 90,
    max: 90,
    default: '39.1'
  },
  about: {
    name: 'about',
    type: 'text',
    default: 'nothing to say'
  },
  rating: {
    name: 'rating',
    type: 'number',
    min: 0,
    max: 5,
    default: '3.5'
  },
  numberOfRatings: {
    name: 'numberOfRatings',
    type: 'number',
    min: 0,
    default: '10'
  },
  // {
  //   name: 'jobOffers',
  //   type: 'array'
  // },
  // {
  //   name: 'jobSeeks',
  //   type: 'array'
  // },
  username: {
    name: 'username',
    type: 'text',
    min: 5,
    max: 20,
    required: true,
    default: 'Tes56789012'
  },
  accessRole: {
    name: 'accessRole',
    type: 'select',
    options: [
      'ROLE_ADMIN', 'ROLE_USER', 'ROLE_GUEST'
    ],
    default: 'ROLE_USER'
  },
  password: {
    name: 'password',
    type: 'password',
    regexp: '^[A-Za-z0-9]*$',
    min: 5,
    required: true,
    default: 'Test12345678901'
  },
  confirmedPassword: {
    name: 'confirmedPassword',
    type: 'password',
    regexp: '^[A-Za-z0-9]*$',
    required: true,
    min: 5,
    default: 'Test12345678901'
  }
}

export default companyDtoObject