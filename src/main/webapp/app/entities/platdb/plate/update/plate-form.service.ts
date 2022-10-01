import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPlate, NewPlate } from '../plate.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlate for edit and NewPlateFormGroupInput for create.
 */
type PlateFormGroupInput = IPlate | PartialWithRequiredKeyOf<NewPlate>;

type PlateFormDefaults = Pick<NewPlate, 'id'>;

type PlateFormGroupContent = {
  id: FormControl<IPlate['id'] | NewPlate['id']>;
  idrestaurant: FormControl<IPlate['idrestaurant']>;
  name: FormControl<IPlate['name']>;
  photo: FormControl<IPlate['photo']>;
  photoContentType: FormControl<IPlate['photoContentType']>;
  price: FormControl<IPlate['price']>;
};

export type PlateFormGroup = FormGroup<PlateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlateFormService {
  createPlateFormGroup(plate: PlateFormGroupInput = { id: null }): PlateFormGroup {
    const plateRawValue = {
      ...this.getFormDefaults(),
      ...plate,
    };
    return new FormGroup<PlateFormGroupContent>({
      id: new FormControl(
        { value: plateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      idrestaurant: new FormControl(plateRawValue.idrestaurant),
      name: new FormControl(plateRawValue.name),
      photo: new FormControl(plateRawValue.photo),
      photoContentType: new FormControl(plateRawValue.photoContentType),
      price: new FormControl(plateRawValue.price),
    });
  }

  getPlate(form: PlateFormGroup): IPlate | NewPlate {
    return form.getRawValue() as IPlate | NewPlate;
  }

  resetForm(form: PlateFormGroup, plate: PlateFormGroupInput): void {
    const plateRawValue = { ...this.getFormDefaults(), ...plate };
    form.reset(
      {
        ...plateRawValue,
        id: { value: plateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PlateFormDefaults {
    return {
      id: null,
    };
  }
}
