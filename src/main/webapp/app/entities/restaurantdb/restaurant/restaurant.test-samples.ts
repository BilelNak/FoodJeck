import { IRestaurant, NewRestaurant } from './restaurant.model';

export const sampleWithRequiredData: IRestaurant = {
  id: '9a4f2ee4-d45a-4122-b55a-c0059e33895f',
};

export const sampleWithPartialData: IRestaurant = {
  id: '09af2038-7917-453b-960d-47b951f0cd51',
  name: 'hardware',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
};

export const sampleWithFullData: IRestaurant = {
  id: 'b0233f4c-6bcd-402b-909d-19ef7a51e68a',
  name: 'deposit up',
  phone: '406-611-9235 x1219',
  adress: 'Program auxiliary red',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
};

export const sampleWithNewData: NewRestaurant = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
