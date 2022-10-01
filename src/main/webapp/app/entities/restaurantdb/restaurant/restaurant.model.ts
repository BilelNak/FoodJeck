export interface IRestaurant {
  id: string;
  name?: string | null;
  phone?: string | null;
  adress?: string | null;
  photo?: string | null;
  photoContentType?: string | null;
}

export type NewRestaurant = Omit<IRestaurant, 'id'> & { id: null };
