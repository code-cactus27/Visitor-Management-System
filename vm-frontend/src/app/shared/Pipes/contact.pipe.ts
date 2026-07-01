import { Pipe, PipeTransform } from '@angular/core';
@Pipe({
  name: 'contact'
})
export class ContactPipe implements PipeTransform {
  transform(value: string): string {
    return value.substring(0,3)+"XXXX"+value.substring(7,10);
  }
}
