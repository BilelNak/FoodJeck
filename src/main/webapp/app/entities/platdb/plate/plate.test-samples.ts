import { IPlate, NewPlate } from './plate.model';

export const sampleWithRequiredData: IPlate = {
  id: '64df0645-00f7-485b-b54d-48b2e291384e',
};

export const sampleWithPartialData: IPlate = {
  id: 'd2add782-c9b5-4f28-9475-d98e6b0e59f8',
  name: 'Cotton Intelligent',
  price: 96009,
};

export const sampleWithFullData: IPlate = {
  id: '1970735a-f3f0-4028-9413-acceca5d4547',
  idrestaurant: 'Mouse Ergonomic',
  name: 'Plastic Zimbabwe',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  price: 69784,
};

export const sampleWithNewData: NewPlate = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
