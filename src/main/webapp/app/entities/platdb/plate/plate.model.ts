export interface IPlate {
  id: string;
  idrestaurant?: string | null;
  name?: string | null;
  photo?: string | null;
  photoContentType?: string | null;
  price?: number | null;
}

export type NewPlate = Omit<IPlate, 'id'> & { id: null };
